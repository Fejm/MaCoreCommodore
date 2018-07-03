package pl.mateam.marpg.api.regular.objects.users;


public interface Gamemaster extends AnyUser {
	boolean hasInteractionEnabled();
	void setInteractionEnabled(boolean newValue);
	
	boolean isVisible();
	void setVisible(boolean newValue, boolean silent);
}
