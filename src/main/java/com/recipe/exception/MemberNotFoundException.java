package com.recipe.exception;

public class MemberNotFoundException extends RuntimeException {
	public MemberNotFoundException(Long memberId) {
		super("Member not found with ID: " + memberId);
	}
}
