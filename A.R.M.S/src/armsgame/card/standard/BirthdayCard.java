package armsgame.card.standard;

public class BirthdayCard extends DebtCollectorCard {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6739199966462076294L;

	@Override
	public String getInternalType() {
		return "action.birthday";
	}
}
