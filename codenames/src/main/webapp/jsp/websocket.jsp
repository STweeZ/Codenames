<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:url var="url" value="/refresh" />
<c:set var="wsurl" value="ws://${pageContext.request.serverName}:${pageContext.request.serverPort}${url}" />

<script type="text/javascript">
    let webSocket = new WebSocket("${wsurl}");
    webSocket.onmessage = function(message){
        if (${game != null} && message.data == ${String.valueOf(game.getHashId())}) {
        	location.reload();
        }
    }
</script>
