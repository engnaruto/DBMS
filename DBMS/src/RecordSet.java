import java.util.HashSet;

@SuppressWarnings("serial")
public class RecordSet extends HashSet<Record>{
	
	private String[] attributesNames;

	public RecordSet(String[] attributes) {
		attributesNames = attributes;
	}

	public String[] getAttributesNames() {
		return attributesNames;
	}

	public void setAttributesNames(String[] attributesNames) {
		this.attributesNames = attributesNames;
	}

}
