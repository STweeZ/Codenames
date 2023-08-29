package fr.univartois.ili.jai.tag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fr.univartois.ili.jai.object.Game;

public class LobbyTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    private Iterator<?> it;
    private String var;

    public void setVar(String var) {
        this.var = var;
    }

    public void setItems(final Object items) {
        if (items instanceof Collection) {
            it = ((Collection<?>) items).iterator();
        } else if (items.getClass().isArray()) {
            it = new Iterator<Object>() {

                private int next = 0;

                @Override
                public boolean hasNext() {
                    return next < Array.getLength(items);
                }

                @Override
                public Object next() {
                	if(!hasNext()){
                	      throw new NoSuchElementException();
                	    }
                    return Array.get(items, next++);
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } else {
            throw new IllegalArgumentException("Items must be of type Collection or Array, found " + items.getClass());
        }
    }

    private int contextUpdated(int continueCode) {
        if (it.hasNext()) {
            Game game = (Game) it.next();
            String style = "";
            if (game.isGameFull()) {
                style += "disabled";
            }
            pageContext.setAttribute(var, game);
            pageContext.setAttribute(var + "_style", style);
            pageContext.setAttribute(var + "_hash", game.getHashId());
            pageContext.setAttribute(var + "_qty", game.getNumberOfPlayer());
            return continueCode;
        }
        return SKIP_BODY;
    }

    @Override
    public int doStartTag() throws JspException {
        return contextUpdated(EVAL_BODY_INCLUDE);
    }

    @Override
    public int doAfterBody() throws JspException {
        return contextUpdated(EVAL_BODY_AGAIN);
    }

    @Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute(var);
        return EVAL_PAGE;
    }

}
