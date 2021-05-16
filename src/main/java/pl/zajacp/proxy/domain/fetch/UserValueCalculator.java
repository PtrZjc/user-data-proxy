package pl.zajacp.proxy.domain.fetch;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Objects.isNull;

public class UserValueCalculator {

    public static Double calculateValue(Integer followers, Integer publicRepos) {
        var x = isNull(followers) || isNull(publicRepos)
                ? null : followers == 0 ? 0 :
                new BigDecimal(6.0 / followers * (2 + publicRepos))
                        .setScale(2, RoundingMode.HALF_DOWN)
                        .doubleValue();
        System.out.println(x);

        return x;
    }
}
