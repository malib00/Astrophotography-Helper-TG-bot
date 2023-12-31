package com.karpov.astrobot.repo;

import com.karpov.astrobot.models.BotState;
import com.karpov.astrobot.models.Chat;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ChatRepository extends CrudRepository<Chat, Long> {

	@Transactional
	@Modifying
	@Query("UPDATE Chat SET botState = ?2 WHERE id = ?1")
	void updateBotStateById(long chatId, BotState botState);

	@Transactional
	@Modifying
	@Query("UPDATE Chat SET latitude = ?2, longitude = ?3 WHERE id = ?1")
	void updateLongitudeAndLatitudeById(long chatId, Double latitude, Double longitude);

	@Transactional
	@Modifying
	@Query("UPDATE Chat SET blockedByUser = ?2 WHERE id = ?1")
	void updateBlockedByUserById(Long chatId, boolean blockedByUser);
}
