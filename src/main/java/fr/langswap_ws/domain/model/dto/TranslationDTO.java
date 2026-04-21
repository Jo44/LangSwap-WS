package fr.langswap_ws.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Translation DTO
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationDTO {

	private Integer id;
	private Integer actionId;
	private String obfuscatedName;
	private String deobfuscatedName;
	private Integer languageId;
	private String characterName;
	private Long creationDate;

	/** Constructor */
	public TranslationDTO() {
		super();
	}

	/** To String
	 *
	 * @return String */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ID : ");
		sb.append(String.valueOf(id));
		sb.append(" - Action ID : ");
		sb.append(String.valueOf(actionId));
		sb.append(" - Obfuscated Name : ");
		if (obfuscatedName != null) {
			sb.append(obfuscatedName);
		} else {
			sb.append("null");
		}
		sb.append(" - Deobfuscated Name : ");
		if (deobfuscatedName != null) {
			sb.append(deobfuscatedName);
		} else {
			sb.append("null");
		}
		sb.append(" - Language ID : ");
		sb.append(String.valueOf(languageId));
		sb.append(" - Character Name : ");
		if (characterName != null) {
			sb.append(characterName);
		} else {
			sb.append("null");
		}
		sb.append(" - Creation Date : ");
		sb.append(String.valueOf(creationDate));
		sb.append(" ]");
		return sb.toString();
	}

	/* Getters / Setters */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getObfuscatedName() {
		return obfuscatedName;
	}

	public void setObfuscatedName(String obfuscatedName) {
		this.obfuscatedName = obfuscatedName;
	}

	public String getDeobfuscatedName() {
		return deobfuscatedName;
	}

	public void setDeobfuscatedName(String deobfuscatedName) {
		this.deobfuscatedName = deobfuscatedName;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public Long getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}
}
