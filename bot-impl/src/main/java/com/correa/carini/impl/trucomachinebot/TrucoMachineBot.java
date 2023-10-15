package com.correa.carini.impl.trucomachinebot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class TrucoMachineBot implements BotServiceProvider {

    private final List<CardRank> strongCards = List.of(CardRank.ACE, CardRank.TWO,CardRank.THREE);
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



        return cardToPlay;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira  = intel.getVira();

        if(hasZapCopas(intel)) return 1;

        if (hasZapManilha(intel)){
            int round = intel.getRoundResults().size();

            // Se for a primeira rodada eu só aceito
            if (round == 0) return 0;
            // Se for a segunda rodada, aceito e peço 6
            if (round == 1) return 1;
        }


        if (hasTwoManilhas(intel)) return 0;

        return -1;
    }

    private TrucoCard getFirstRoundCard(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        TrucoCard vira  = intel.getVira();

        TrucoCard greatestCard = getGreatestCard(cards, vira);

        if(intel.getOpponentCard().isPresent()){
            if(greatestCard.compareValueTo(intel.getOpponentCard().get(), vira) > 0){
                return greatestCard;
            }

            return getLowestCard(cards, vira);
        }


        return greatestCard;
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

    private boolean hasSrongCards(GameIntel intel){
        return intel.getCards().stream().anyMatch(card -> strongCards.contains(card.getRank()));
    }

    private boolean hasZapCopas(GameIntel intel){
        boolean zap = false;
        boolean copas = false;
        for (TrucoCard card : intel.getCards()){
            if (card.isZap(intel.getVira())){
                zap = true;
            }

            if (card.isCopas(intel.getVira())){
                copas = true;
            }
        }

        if (zap && copas) return true;
        return false;
    }


    private boolean hasTwoManilhas(GameIntel intel){
        int manilhas = 0;

        for (TrucoCard card : intel.getCards()){
            if (card.isManilha(intel.getVira())){
                manilhas += 1;
            }
        }

        if (manilhas >= 2) return true;
        return false;
    }

    private boolean hasZapManilha(GameIntel intel){
        boolean zap = false;
        int manilhas = 0;

        for (TrucoCard card : intel.getCards()){
            if (card.isZap(intel.getVira())){
                zap = true;
            }
            if (card.isOuros(intel.getVira()) || card.isEspadilha(intel.getVira()) || card.isCopas(intel.getVira())){
                manilhas += 1;
            }
        }

        if (zap && manilhas >= 1) return true;
        return false;
    }
}


