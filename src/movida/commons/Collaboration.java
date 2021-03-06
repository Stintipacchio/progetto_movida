package movida.commons;

import java.util.ArrayList;

public class Collaboration {

	Person actorA;
	Person actorB;
	public ArrayList<Movie> movies;
	
	public Collaboration(Person actorA, Person actorB, ArrayList<Movie> mv) {
		this.actorA = actorA;
		this.actorB = actorB;
		this.movies = new ArrayList<>(mv);
	}

	public Person getActorA() {
		return actorA;
	}

	public Person getActorB() {
		return actorB;
	}

	public Double getScore(){
		
		Double score = 0.0;
		
		for (Movie m : movies)
			score += m.getVotes();
		
		return score / movies.size();
	}
	
}
