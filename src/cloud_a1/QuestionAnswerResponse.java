package cloud_a1;

import java.io.Serializable;

public class QuestionAnswerResponse implements Serializable {
	private static final long serialVersionUID = -2751306541198860923L;
	int id;
	boolean yes;
	public QuestionAnswerResponse(int key, boolean yes) {
		this.id = key;
		this.yes = yes;
	}
}
