package com.github.kudr9tov.epaper.exceptions;

public class FileUploadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileUploadException(Exception e) {
		super(e);
	}
}
