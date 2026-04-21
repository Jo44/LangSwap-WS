package fr.langswap_ws.domain.model.entity;

import java.sql.Timestamp;
import fr.langswap_ws.domain.model.base.Language;
import fr.langswap_ws.domain.model.base.LanguageConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Translation entity
 *
 * @author Jo44
 * @version 1.0 (21/04/2026)
 * @since 21/04/2026 */
@Entity
@Table(name = "obfuscated_translations")
public class Translation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "action_id", nullable = false)
	private Integer actionId;

	@Column(name = "obfuscated_name", length = 255, nullable = false)
	private String obfuscatedName;

	@Column(name = "deobfuscated_name", length = 255, nullable = false)
	private String deobfuscatedName;

	@Convert(converter = LanguageConverter.class)
	@Column(name = "language_id", nullable = false)
	private Language languageId;

	@Column(name = "character_name", length = 255, nullable = false)
	private String characterName;

	@Column(name = "creation_date", nullable = false)
	private Timestamp creationDate;

	/** Constructor */
	public Translation() {
		super();
	}

	/** Constructor
	 *
	 * @param id
	 * @param actionId
	 * @param obfuscatedName
	 * @param deobfuscatedName
	 * @param languageId
	 * @param characterName
	 * @param creationDate */
	public Translation(Integer id, Integer actionId, String obfuscatedName, String deobfuscatedName,
			Language languageId, String characterName, Timestamp creationDate) {
		super();
		this.id = id;
		this.actionId = actionId;
		this.obfuscatedName = obfuscatedName;
		this.deobfuscatedName = deobfuscatedName;
		this.languageId = languageId;
		this.characterName = characterName;
		this.creationDate = creationDate;
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

	public Language getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Language languageId) {
		this.languageId = languageId;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
}
