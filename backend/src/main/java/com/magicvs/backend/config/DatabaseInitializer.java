package com.magicvs.backend.config;

import com.magicvs.backend.repository.CardRepository;
import com.magicvs.backend.repository.MetaDeckRepository;
import com.magicvs.backend.repository.AchievementRepository;
import com.magicvs.backend.model.Achievement;
import com.magicvs.backend.model.AchievementTrigger;
import com.magicvs.backend.service.ScryfallService;
import com.magicvs.backend.service.MetaScrapingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final CardRepository cardRepository;
    private final ScryfallService scryfallService;
    private final MetaDeckRepository metaDeckRepository;
    private final AchievementRepository achievementRepository;
    private final MetaScrapingService metaScrapingService;

    public DatabaseInitializer(CardRepository cardRepository, 
                               ScryfallService scryfallService,
                               MetaDeckRepository metaDeckRepository,
                               MetaScrapingService metaScrapingService,
                               AchievementRepository achievementRepository) {
        this.cardRepository = cardRepository;
        this.scryfallService = scryfallService;
        this.metaDeckRepository = metaDeckRepository;
        this.metaScrapingService = metaScrapingService;
        this.achievementRepository = achievementRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (cardRepository.count() == 0) {
            logger.info("Base de datos de cartas vacía. Iniciando importación automática por lotes (Batching activo)...");
            long startTime = System.currentTimeMillis();
            
            scryfallService.importStandardCards();
            
            long endTime = System.currentTimeMillis();
            logger.info("Importación automática completada en {} segundos.", (endTime - startTime) / 1000);
        } else {
            logger.info("Base de datos de cartas ya poblada con {} registros. Omitiendo importación inicial.", cardRepository.count());
        }

        if (metaDeckRepository.count() == 0) {
            logger.info("Base de datos de Metajuego vacía. Iniciando scraping automático inicial de MTGGoldfish...");
            metaScrapingService.syncMetagame("30");
        } else {
            logger.info("Metajuego ya inicializado con {} mazos. Esperando al demonio nocturno para actualizar.", metaDeckRepository.count());
        }

        if (achievementRepository.count() == 0) {
            logger.info("Inicializando logros por defecto...");

            Achievement a1 = new Achievement();
            a1.setCode("first_deck");
            a1.setName("Primer mazo creado");
            a1.setDescription("Has creado tu primer mazo.");
            a1.setTrigger(AchievementTrigger.DECK_CREATED);
            a1.setThreshold(1);
            a1.setBadgeUri(null);

            Achievement a2 = new Achievement();
            a2.setCode("ten_decks");
            a2.setName("10 mazos creados");
            a2.setDescription("Has creado 10 mazos.");
            a2.setTrigger(AchievementTrigger.DECK_CREATED);
            a2.setThreshold(10);
            a2.setBadgeUri(null);

            Achievement w1 = new Achievement();
            w1.setCode("first_win");
            w1.setName("Primera victoria");
            w1.setDescription("Has ganado tu primera partida.");
            w1.setTrigger(AchievementTrigger.GAMES_WON);
            w1.setThreshold(1);
            w1.setBadgeUri(null);

            Achievement w10 = new Achievement();
            w10.setCode("ten_wins");
            w10.setName("10 victorias");
            w10.setDescription("Has alcanzado 10 victorias.");
            w10.setTrigger(AchievementTrigger.GAMES_WON);
            w10.setThreshold(10);
            w10.setBadgeUri(null);

            achievementRepository.saveAll(java.util.List.of(a1, a2, w1, w10));
        }
    }
}
