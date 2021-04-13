import java.util.*;
/**
 * @author Matthew
 *
 */
public class Category {
	private String alias;
	private String title;
	private Set<String> parentAliases;
	
	/**
	 * @param alias
	 * @param title
	 * @param parentAliases
	 */
	public Category(String alias, String title, Set<String> parentAliases) {
		this.alias = alias;
		this.title = title;
		this.parentAliases = parentAliases;
	}
	
	public Category(String alias, String title) {
		this.alias = alias;
		this.title = title;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param parentAlias
	 * @return whether or not the inputted parentAlias is a parent of this category
	 */
	public boolean hasParentAlias(String parentAlias) {
		return this.parentAliases.contains(parentAlias);
	}
}
