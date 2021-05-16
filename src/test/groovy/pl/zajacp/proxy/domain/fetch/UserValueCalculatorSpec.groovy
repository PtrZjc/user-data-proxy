package pl.zajacp.proxy.domain.fetch

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static pl.zajacp.proxy.domain.fetch.UserValueCalculator.calculateValue

@Subject(UserValueCalculator)
class UserValueCalculatorSpec extends Specification {

    @Unroll
    def "Validates f(#followers, #publicRepos) == #expectedValue"() {
        when:
        Double calculationValue = calculateValue(followers, publicRepos)

        then:
        calculationValue == expectedValue

        where:
        followers | publicRepos || expectedValue
        0         | 0           || 0
        1         | 1           || 18
        2         | 2           || 12
        3         | 3           || 10
        4         | 4           || 9
        5         | 5           || 8.4
        6         | 6           || 8
        7         | 7           || 7.71
        8         | 8           || 7.5
        9         | 9           || 7.33
        10        | 10          || 7.2
        null      | 1           || null
        1         | null        || null
    }
}
