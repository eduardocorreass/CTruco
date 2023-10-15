package com.correa.carini.impl.trucomachinebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class TrucoMachineBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int round = intel.getRoundResults().size();

        CardToPlay cardToPlay = null;

        if(round == 0) cardToPlay = CardToPlay.of(getFirstRoundCard(intel));
        if(round == 1) cardToPlay = CardToPlay.of(getSecondRoundCard(intel));
        if(round == 2) cardToPlay = CardToPlay.of(getThirdRoundCard(intel));



        return cardToPlay;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    private TrucoCard getFirstRoundCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        TrucoCard vira  = intel.getVira();

        TrucoCard greatestCard = getGreatestCard(cards, vira);

        if(intel.getOpponentCard().isPresent()){
            if(greatestCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0){
                return getMinimalGreaterCard(cards, vira, intel.getOpponentCard().get());
            }

            return getLowestCard(cards, vira);
        }


        return greatestCard;
    }

    private TrucoCard getSecondRoundCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        TrucoCard vira  = intel.getVira();

        if(intel.getOpponentCard().isPresent()) {
            TrucoCard minimalGreaterCard = getMinimalGreaterCard(cards, vira, intel.getOpponentCard().get());

            if(minimalGreaterCard != null) return minimalGreaterCard;
        }

        return getLowestCard(cards, vira);
    }

    private TrucoCard getThirdRoundCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());

        return cards.get(0);
    }

    private TrucoCard getGreatestCard( List<TrucoCard> cards, TrucoCard vira){
        TrucoCard greatestCard = cards.get(0);

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(greatestCard, vira);
            if (comparison > 0) {
                greatestCard = card;
            }
        }

        return greatestCard;
    }

    private TrucoCard getLowestCard( List<TrucoCard> cards, TrucoCard vira){
        TrucoCard lowestCard = cards.get(0);

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(lowestCard, vira);
            if (comparison < 0) {
                lowestCard = card;
            }
        }

        return lowestCard;
    }

    private TrucoCard getMinimalGreaterCard( List<TrucoCard> cards, TrucoCard vira, TrucoCard opponentCard){
        TrucoCard minimalGreaterCard = null;

        for (TrucoCard card : cards) {
            int comparison = card.compareValueTo(opponentCard, vira);
            if (comparison > 0) {
                if (minimalGreaterCard == null || card.compareValueTo(minimalGreaterCard, vira) < 0) {
                    minimalGreaterCard = card;
                }
            }
        }

        return minimalGreaterCard;
    }
}
