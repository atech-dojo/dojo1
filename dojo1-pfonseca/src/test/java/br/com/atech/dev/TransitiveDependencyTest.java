package br.com.atech.dev;

import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import br.com.atech.dev.exception.CyclicDependencyException;

public class TransitiveDependencyTest {
	
	@Test
	public void souldResolveWithNull(){
		String input = null;
		
		String result = new TransitiveDependency(input).toString();
		Assert.assertEquals("", result);
	}
	
	@Test
	public void souldResolveWithNoDepedency(){
		String input = "A\nB\nC\nD\nE\nF";
		
		String result = new TransitiveDependency(input).toString();
		Assert.assertEquals("A\nB\nC\nD\nE\nF", result);
	}
	
	@Test
	public void souldResolveAnotherSimpleDepedency(){
		String input = "A B";
		
		String result = new TransitiveDependency(input).toString();
		Assert.assertEquals("A B", result);
	}
	
	@Test
	public void souldResolveDepedencyWithThreeParameters(){
		String input = "A B\nB C";
		
		String result = new TransitiveDependency(input).toString();
		Assert.assertEquals("A B C\nB C", result);
	}
	
	@Test(expected=CyclicDependencyException.class)
	public void souldResolveDepedencyCyclicDependency(){
		String input = "A B\nB A";
		
		new TransitiveDependency(input).toString();
	}
	
	@Test
	public void souldResolveDepedencyWithFourParameters(){
		String input = "A B\nB C\nC D";
		
		String result = new TransitiveDependency(input).toString();
		Assert.assertEquals("A B C D\nB C D\nC D", result);
	}
	
	@Test
	public void souldResolveDepedencyExample(){
		String input = 	  "A B C\n"+
						  "B C E\n"+
						  "C G\n"+
						  "D A F\n"+
						  "E F\n"+
						  "F H";
		
		String result = new TransitiveDependency(input).toString();
		Assert.assertEquals(  "A B C E F G H\n"+
							  "B C E F G H\n"+
							  "C G\n"+
							  "D A B C E F G H\n"+
							  "E F H\n"+
							  "F H", result);
	}
	
	@Test
	public void souldParseASimpleDepedency(){
		String input = "A";
		
		TransitiveDependency transitiveDependency = new TransitiveDependency(input);
		
		Map<String, Set<String>> dependencyTree = transitiveDependency.getDependencyTree();
		Assert.assertEquals(1, dependencyTree.size());
	}
	
	@Test
	public void souldParseAnotherSimpleDepedency(){
		String input = "A B";
		
		TransitiveDependency transitiveDependency = new TransitiveDependency(input);
		Map<String, Set<String>> result = transitiveDependency.getDependencyTree();
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1, result.get("A").size());
	}
	
	@Test
	public void souldParseDepedencyWithThreeParameters(){
		String input = "A B\nB C";
		
		TransitiveDependency transitiveDependency = new TransitiveDependency(input);
		Map<String, Set<String>> result = transitiveDependency.getDependencyTree();
		
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(2, result.get("A").size());
		Assert.assertEquals(1, result.get("B").size());
	}
}
