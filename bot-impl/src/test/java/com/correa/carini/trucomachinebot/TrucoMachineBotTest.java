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

    @Test
    @DisplayName("Should raise if last round card is greater than opponent card")
    void ShouldRaiseIfLastRoundCardIsGreaterThanOpponentCard() {
        TrucoCard vira = TrucoCard.of(ACE, SPADES);
        List<TrucoCard> card = List.of(
                TrucoCard.of(TWO, CLUBS)
        );
        List<GameIntel.RoundResult> roundResults = List.of(
                GameIntel.RoundResult.WON,
                GameIntel.RoundResult.LOST
        );

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(roundResults, List.of(TrucoCard.of(FOUR, CLUBS)), vira, 1)
                .botInfo(card, 0)
                .opponentScore(0)
                .opponentCard(TrucoCard.of(FOUR, CLUBS));

        boolean decideIfRaises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(decideIfRaises);
    }

    @Test
    @DisplayName("Should raise if have manilha and 3 and score difference is greater than 2")
    void ShouldRaiseIfHaveManilhaAnd3AndScoreDifferenceIsGreaterThan2() {
        TrucoCard vira = TrucoCard.of(FIVE, SPADES);
        List<TrucoCard> card = List.of(
                TrucoCard.of(SIX, DIAMONDS),
                TrucoCard.of(THREE, CLUBS)
        );

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(card, 3)
                .opponentScore(0);

        boolean decideIfRaises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertTrue(decideIfRaises);
    }

    @Test
    @DisplayName("Should not raise if score difference is lower than 3 and has low cards")
    void ShouldNotRaiseIfScoreDifferenceIsLowerThan3AndHasLowCards() {
        TrucoCard vira = TrucoCard.of(TWO, SPADES);
        List<TrucoCard> card = List.of(
                TrucoCard.of(SIX, DIAMONDS),
                TrucoCard.of(FIVE, CLUBS)
        );

        GameIntel.StepBuilder stepBuilder = GameIntel.StepBuilder.with()
                .gameInfo(List.of(), List.of(), vira, 1)
                .botInfo(card, 2)
                .opponentScore(0);

        boolean decideIfRaises = new TrucoMachineBot().decideIfRaises(stepBuilder.build());
        assertFalse(decideIfRaises);
    }
}
