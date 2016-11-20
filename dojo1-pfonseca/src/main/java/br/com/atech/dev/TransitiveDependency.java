package br.com.atech.dev;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.com.atech.dev.exception.CyclicDependencyException;

public class TransitiveDependency {

	private Map<String, Set<String>> dependencyTree;
	
	public TransitiveDependency(String input){
		this.dependencyTree = parse(input);
		solveDependencies();
	}

	public String toString() {
		
		StringBuilder result = new StringBuilder();
		
		this.dependencyTree.forEach((key, values) -> {
			result.append(key);
			values.forEach(dependency -> result.append(" ").append(dependency));
			result.append("\n");
		});
		
		return result.toString().trim();
	}

	public Map<String, Set<String>> parse(String input) {
		
		Map<String, Set<String>> dependencyTree = new HashMap<>();
		
		if(input != null){
			for (String row : input.split("\n")) {
				
				String[] values = row.split(" ");
				String key = values[0];
				Set<String> dependencies = new HashSet<>();
				
				for(int i=1; i < values.length; i++){
					dependencies.add(values[i]);
				}
				
				dependencyTree.put(key, dependencies);
			}
		}
		
		return dependencyTree;
	}
	
	
	private Set<String> solveDependencies(String root) {
		
		Set<String> dependencies = new HashSet<>();
		
		if(dependencyTree.containsKey(root)){
			dependencies = dependencyTree.get(root);
			
			if(dependencies.contains(root))
				throw new CyclicDependencyException();
			
			for(String dependency: new HashSet<>(dependencies)){
				Set<String> localDependencies = dependencyTree.get(dependency);
				
				if(localDependencies != null && !localDependencies.isEmpty())
					dependencies.addAll(localDependencies);
				
				dependencies.addAll(solveDependencies(dependency));
			}
		}
		
		return dependencies;
	}
	
	private void solveDependencies() {
		dependencyTree.forEach((key, values) -> {
			solveDependencies(key);
		});
	}

	public Map<String, Set<String>> getDependencyTree() {
		return dependencyTree;
	}

}
