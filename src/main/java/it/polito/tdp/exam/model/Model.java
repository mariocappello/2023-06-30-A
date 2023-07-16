package it.polito.tdp.exam.model;
	
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.exam.db.BaseballDAO;

	public class Model {
		BaseballDAO dao;
		Map<Integer, Team> idMap;
		Map<String,People> idMapPeople;
		Graph<Integer,DefaultWeightedEdge> grafo;
		
		
		public Model() {
			dao=new BaseballDAO();
			idMap=new HashMap<>();
			dao.readAllTeamsMap(idMap);
			idMapPeople = new HashMap<>();
			dao.readAllPlayers(idMapPeople);
			
		}
		
		
		public void creaGrafo(Team team) {
			grafo = new SimpleWeightedGraph<Integer,DefaultWeightedEdge>(DefaultWeightedEdge.class);
			
			List<Integer> listaVertici = dao.getVertici(team);
			Graphs.addAllVertices(grafo, listaVertici);
			
			
			List<Arco> listaArchi = dao.getArchiGrafo(team,idMapPeople);
			for(Arco a1 : listaArchi) {
				for(Arco a2 : listaArchi) {
					if(a1.getYear()!=a2.getYear()) {
						double peso=0;
							if(a1.getSquadCode().equals(a2.getSquadCode())) {
								//Set<String> unione = new HashSet<>(a1.getPlayers());
								//unione.addAll(a2.getPlayers());
								for(String s1 : a1.getPlayers()) {
									for(String s2 : a2.getPlayers()) {
										if(s1.equals(s2)) {
											peso+=1;
										}
									}
								}
							Graphs.addEdgeWithVertices(grafo, a1.getYear(), a2.getYear(), peso);
							//eventualmente si può tener conto dell'adiacenza
							//Adiacenza a = new Adiacenza(a1.getYear(), a2.getYear(), peso);
						}	
					}
				}
			}
		}
		
		
		
		public List<Integer> getVertici(Team team) {
			return dao.getVertici(team);
		}
		
		
		public List<Team> readAllTeams() {
			return dao.readAllTeams();
		}
		
		public int getNumeroVertici() {
			if(grafo!=null) {
				return grafo.vertexSet().size();
			}
			else {
				return 0;	
			}
		}
		
		public int getNumeroArchi() {
			if(grafo!=null) {
				return grafo.edgeSet().size();
			}
			else {
				return 0;	
			}
		}

		public boolean grafoCreato() {
			if(grafo!=null) {
				return true;
			}
			else
			return false;
		}
		
		public Set<DefaultWeightedEdge> listaVertici() {
			return grafo.edgeSet();
		}


		public List<Adiacenza> calcolaAdiacenti(int anno) {
			
			List<Adiacenza> adiacenza = new ArrayList<>();
			
			Set<DefaultWeightedEdge> archi = grafo.edgesOf(anno);
			
			for(DefaultWeightedEdge e : archi) {
				Adiacenza a = new Adiacenza (grafo.getEdgeSource(e), grafo.getEdgeTarget(e),grafo.getEdgeWeight(e));
				adiacenza.add(a);
			}
			Collections.sort(adiacenza);
			return adiacenza;
		}
		
		
		
		
	}
