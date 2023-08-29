package fr.univartois.ili.jai.tag;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class StatisticsTag extends TagSupport {
    private static final long serialVersionUID = 1L;

    private Iterator<?> it;
    private String var;
    private String filter;
    private int index;

    public void setVar(String var) {
        this.var = var;
    }

    public void setFilter(String filter) {
        this.filter = filter;
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
    
    private String fromListToString(List<String> stats) {
    	String str = "";
    	for(String stat: stats) {
    		if (str != "")
    			str = str.concat(", ");
    		str = str.concat(stat);
    	}
    	return str;
    }

    private int contextUpdated(int continueCode) {
        if (it.hasNext()) {
            List<List<String>> stats = (List<List<String>>) it.next();
            pageContext.setAttribute(var + "_nb", index++);
            pageContext.setAttribute(var + "_first", fromListToString(stats.get(0)));
            pageContext.setAttribute(var + "_second", filter.equals("games") ? fromListToString(stats.get(1)) : stats.get(1));
            pageContext.setAttribute(var + "_third",
                    filter.equals("games") ? (stats.get(2).equals(stats.get(0)) ? "blue" : "red") : "");
            pageContext.setAttribute(var + "_blue_search", stats.get(0));
            pageContext.setAttribute(var + "_red_search", stats.get(1));
            return continueCode;
        }
        return SKIP_BODY;
    }

    @Override
    public int doStartTag() throws JspException {
        index = 1;
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
