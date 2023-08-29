<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ili" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="codeili" uri="http://www.univ-artois.fr/master2ili/jai"%>
<jsp:useBean id="statistics" class="utils.Statistics" scope="request"/>

<ili:page title="Statistics - Codenames">
	<jsp:body>
       <main>
        <h1 class="pb-4">Statistics</h1>
        <div class="pb-4">
	        <form method=post>
			  	<button type="submit" class="m-2 btn btn-light" name="filter" value="games" data-container="body" data-toggle="popover" data-placement="top">Last games</button>
				<button type="submit" class="m-2 btn btn-light" name="filter" value="teams" data-container="body" data-toggle="popover" data-placement="right">Best teams</button>
				<button type="submit" class="m-2 btn btn-light" name="filter" value="players" data-container="body" data-toggle="popover" data-placement="bottom">Best players</button>
			</form>
		</div>
		<div>
			<table class="table table-dark">
				<thead>
					<tr>
						<c:choose>
							<c:when test="${requestScope.filter == 'teams'}">
								<th scope="col">#</th>
								<th scope="col">Team</th>
								<th scope="col">Number of wins</th>
							</c:when>
							<c:when test="${requestScope.filter == 'players'}">
								<th scope="col">#</th>
								<th scope="col">Username</th>
								<th scope="col">Number of wins</th>
							</c:when>
							<c:otherwise>
								<th scope="col">Team Blue</th>
								<th scope="col">Team Red</th>
							</c:otherwise>
						</c:choose>
					</tr>
				</thead>
				<tbody>
					<codeili:stats var="stat" items="${requestScope.filter == 'teams' ? statistics.getStatisticsTeams() : (requestScope.filter == 'players' ? statistics.getStatisticsPlayers() : statistics.getStatisticsGames())}" filter="${requestScope.filter}">
						<tr>
							<c:if test="${requestScope.filter == 'players' || requestScope.filter == 'teams'}">
								<th scope="row">${stat_nb}</th>
							</c:if>
							<td class="${username != null && stat_blue_search.contains(username) ? 'winner-player' : ''}">${stat_first} ${stat_third == 'blue' ? '<i class="bi bi-trophy-fill role-icon winner-team"></i>' : ''}</td>
							<td class="${username != null && stat_red_search.contains(username) ? 'winner-player' : ''}">${stat_second} ${stat_third == 'red' ? '<i class="bi bi-trophy-fill role-icon winner-team"></i>' : ''}</td>
						</tr>
					</codeili:stats>
				</tbody>
			</table>
		</div>
      </main>
   </jsp:body>
</ili:page>
