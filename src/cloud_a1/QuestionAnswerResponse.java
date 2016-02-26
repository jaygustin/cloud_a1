package cloud_a1;

import java.io.Serializable;

public class QuestionAnswerResponse implements Serializable {
	private static final long serialVersionUID = -2751306541198860923L;
	int id;
	boolean yes;
	public QuestionAnswerResponse(int key, String yesOrNo) {
		this.id = key;
		this.yes = yesOrNo.contains("yes") || yesOrNo.contains("true");
	}
}
