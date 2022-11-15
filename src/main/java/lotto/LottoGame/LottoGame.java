package lotto.LottoGame;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lotto.exception.IllegalPriceUnitException;
import lotto.lotto.Lotto;
import lotto.lotto.LottoGenerator;
import lotto.winning.WinningNumber;
import lotto.winning.WinningResult;
import lotto.winning.WinningResultCalculator;
import lotto.winning.WinningStatistical;

public class LottoGame {
    private final WinningResultCalculator winningResultCalculator = new WinningResultCalculator();

    private final LottoGenerator lottoGenerator;

    private final LottoGamePrinter lottoGamePrinter;


    public LottoGame(LottoGenerator lottoGenerator, LottoGamePrinter lottoGamePrinter) {
        this.lottoGenerator = lottoGenerator;
        this.lottoGamePrinter = lottoGamePrinter;
    }

    public void run() {
        // 금액을 입력 받는다.
        Integer lottoBuyingPrice = readLottoBuyingPrice();
        // 입력 받은 금액으로 로또를 발행한다.
        List<Lotto> lottos = issueLottos(lottoBuyingPrice);
        WinningNumber winningNumber = readWinningNumber();

        List<WinningResult> winningResults = lottos.stream()
                .map(lotto -> winningResultCalculator.calculate(winningNumber, lotto)).collect(Collectors.toList());
        WinningStatistical winningStatistical = new WinningStatistical();
        winningResults.forEach(winningStatistical::addWinningResult);
        lottoGamePrinter.printWinningStatistical(winningStatistical);
    }

    private WinningNumber readWinningNumber() {
        lottoGamePrinter.printRequestWinningNumber();
        List<Integer> lottoNumbers = Arrays.stream(Console.readLine().split(",")).map(Integer::parseInt)
                .collect(Collectors.toList());

        lottoGamePrinter.printRequestBonusNumber();
        Integer bonusNumber = Integer.parseInt(Console.readLine());
        return new WinningNumber(lottoNumbers, bonusNumber);
    }

    private List<Lotto> issueLottos(Integer lottoBuyingPrice) {
        List<Lotto> lottos = Stream.generate(lottoGenerator::generate).limit(lottoBuyingPrice / 1000)
                .collect(Collectors.toList());

        lottoGamePrinter.printBoughtLottos(lottos);
        return lottos;
    }

    private Integer readLottoBuyingPrice() throws IllegalPriceUnitException {
        lottoGamePrinter.printRequestBuyingPrice();

        Integer lottoBuyingPrice = Integer.parseInt(Console.readLine());
        validateLottoBuyingPrice(lottoBuyingPrice);
        return lottoBuyingPrice;
    }

    private void validateLottoBuyingPrice(Integer readPrice) throws IllegalPriceUnitException {
        if (readPrice % 1000 != 0) {
            throw new IllegalPriceUnitException();
        }
    }
}
