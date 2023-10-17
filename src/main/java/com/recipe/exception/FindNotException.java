package com.recipe.exception;

import java.util.NoSuchElementException;

public class FindNotException extends NoSuchElementException {
	   public FindNotException(String message) {
	        super(message);
	    }
	
}
