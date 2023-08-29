<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ili" tagdir="/WEB-INF/tags"%>

<ili:page title="Rules - Codenames">
	<jsp:body>
        <main>
	        <h1 class="pb-4">Rules</h1>
	        <div>
		        <p class="lead">
			        JAI's project 2022 is a web implementation of the "codename" board game.</br>
					Two teams (say A and B) compete on a grid of 25 words. Each team consists of a spy who must make the other members of the team (the decoders) guess a set of words (9 for the starting team, 8 for the other) among 25 possibilities.
					The spy can only give two pieces of information to the decoders: a word that is not on the grid and the number of words to guess.</br>
					7 words in the grid are neutral. If a team mentions them, they hand over to the other team.
					One of the words in the grid is the word assassin. If the latter is pronounced by a team, this one loses.
					Otherwise, the team that guessed all of their words first wins.</br>
					For more information, visit <a href="https://www.regledujeu.fr/codenames/" class="text-white" target="_blank">regledujeu.fr</a>.
				</p>
            </div>
	      </main>
    </jsp:body>
</ili:page>
