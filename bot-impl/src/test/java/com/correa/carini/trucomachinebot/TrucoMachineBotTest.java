package com.correa.carini.trucomachinebot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.correa.carini.impl.trucomachinebot.TrucoMachineBot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.*;
import static com.bueno.spi.model.CardRank.SEVEN;
import static com.bueno.spi.model.CardSuit.*;
import static org.junit.jupiter.api.Assertions.*;

public class TrucoMachineBotTest {

    @Test
    @DisplayName("Should not raise if bot score is equal to 11")
    void ShouldNotRaiseIfBotScoreIsEqualTo11() {
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(List.of(), 11);

        boolean decideIfRaises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertFalse(decideIfRaises);
    }

    @Test
    @DisplayName("Should not raise if opponent score is equal to 11")
    void ShouldNotRaiseIfOpponentScoreIsEqualTo11() {
        GameIntel.StepBuilder stepBuilder = (GameIntel.StepBuilder) GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), TrucoCard.of(ACE, SPADES), 1)
                .botInfo(List.of(), 0)
                .opponentScore(11);

        boolean decideIfRaises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertFalse(decideIfRaises);
    }

    @Test
    @DisplayName("Should raise if has zap and manilha")
    void ShouldRaiseIfHasZapAndManilha() {
        TrucoCard vira = TrucoCard.of(ACE, SPADES);
        List<TrucoCard> cards = List.of(
                TrucoCard.of(TWO, CLUBS),
                TrucoCard.of(TWO, DIAMONDS),
                TrucoCard.of(FIVE, HEARTS)
        );

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(cards, 0)
                .opponentScore(0);

        boolean decideIfRaises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(decideIfRaises);
    }
}
