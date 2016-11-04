package pl.confectionary.db;


public class ApplicationException extends RuntimeException {
	
	public ApplicationException(Exception e) {
		super(e);
	}

	public ApplicationException() {
		super();
	}

	private static final long serialVersionUID = -9047344758992548664L;
}
