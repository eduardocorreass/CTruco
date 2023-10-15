package com.correa.carini.trucomachinebot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.correa.carini.impl.trucomachinebot.TrucoMachineBot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bueno.spi.model.CardRank.ACE;
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
}
