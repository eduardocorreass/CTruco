package com.correa.carini.impl.trucomachinebot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.CardRank.THREE;
import static com.bueno.spi.model.CardRank.TWO;

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
        if(round == 1) cardToPlay = CardToPlay.of(getSecondRoundCard(intel));
        if(round == 2) cardToPlay = CardToPlay.of(getThirdRoundCard(intel));



        return cardToPlay;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        TrucoCard vira  = intel.getVira();
        List<TrucoCard> cards = intel.getCards();


        if(hasZapCopas(intel)) return 1;

        if (hasZapManilha(intel)){
            int round = intel.getRoundResults().size();

            // Se for a primeira rodada eu só aceito
            if (round == 0) return 0;
            // Se for a segunda rodada, aceito e peço 6
            if (round == 1) return 1;
        }

        //if (hasTwoManilhas(intel)) return 0;

        if (roundResults.size() == 0) {
            List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();

            if (manilhas.isEmpty()) {
                List<TrucoCard> ternos = cards.stream().filter(card -> card.getRank().equals(THREE)).toList();
                List<TrucoCard> duques = cards.stream().filter(card -> card.getRank().equals(TWO)).toList();
                if (ternos.size() == 3) {
                    return 0;
                } else if (ternos.size() == 2) {
                    return 0;
                } else if (ternos.size() == 1 && duques.size() == 2) {
                    return 0;
                } else {
                    return -1;
                }

            }

            if (manilhas.size() == 1) {
                Optional<TrucoCard> ternos = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
                Optional<TrucoCard> duques = cards.stream().filter(card -> card.getRank().equals(TWO)).findFirst();

                if (ternos.isPresent()) {
                    return 0;
                } else {
                    for (TrucoCard card : intel.getCards()) {
                        if (card.isZap(intel.getVira()) || card.isCopas(intel.getVira())) {
                            if (duques.isPresent()) {
                                return 0;
                            }
                        }
                    }
                    return -1;
                }
            }
            if (manilhas.size() == 2) return 0;
            if (manilhas.size() == 3) return 0;
        } else if (roundResults.size() == 1){

            if (roundResults.get(0).equals(GameIntel.RoundResult.LOST)) {
                List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();

                if (manilhas.isEmpty()) {
                    return -1;
                }
                else if (manilhas.size() == 1) {
                    Optional<TrucoCard> ternos = cards.stream().filter(card -> card.getRank().equals(THREE)).findFirst();
                    Optional<TrucoCard> duques = cards.stream().filter(card -> card.getRank().equals(TWO)).findFirst();
                    for (TrucoCard card : intel.getCards()) {
                        if (card.isZap(intel.getVira()) || card.isCopas(intel.getVira())) {
                            if (ternos.isPresent() || duques.isPresent()){
                                return 0;
                            } else{
                                return -1;
                            }
                        }
                    }
                }
                else if (manilhas.size() == 2) return 0;
                else return -1;
            }
            if (roundResults.get(0).equals(GameIntel.RoundResult.WON)) {
                List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();

                if (manilhas.isEmpty()) {
                    List<TrucoCard> ternos = cards.stream().filter(card -> card.getRank().equals(THREE)).toList();
                    List<TrucoCard> duques = cards.stream().filter(card -> card.getRank().equals(TWO)).toList();
                    if (ternos.size() > 0) {
                        return 0;
                    } else if (duques.size() > 0) {
                        return 0;
                    } else {
                        return -1;
                    }

                } else{
                    return 0;
                }
            }
        } else if (roundResults.size() == 2){
            if (roundResults.get(0).equals(GameIntel.RoundResult.WON)){
                List<TrucoCard> manilhas = cards.stream().filter(card -> card.isManilha(vira)).toList();
                if (manilhas.isEmpty()) {
                    return -1;
                } else{
                    return 0;
                }
            }
        }
        return -1;
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

    private boolean hasZapManilha(GameIntel intel) {
        boolean zap = false;
        int manilhas = 0;

        for (TrucoCard card : intel.getCards()) {
            if (card.isZap(intel.getVira())) {
                zap = true;
            }
            if (card.isOuros(intel.getVira()) || card.isEspadilha(intel.getVira()) || card.isCopas(intel.getVira())) {
                manilhas += 1;
            }
        }

        if (zap && manilhas >= 1) return true;
        return false;
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


