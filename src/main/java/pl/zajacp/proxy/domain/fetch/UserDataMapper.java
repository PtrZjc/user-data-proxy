package pl.zajacp.proxy.domain.fetch;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajacp.proxy.domain.fetch.model.GithubUserData;
import pl.zajacp.proxy.model.UserDataResponse;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserDataMapper {

    UserDataResponse fromGithubResponse(GithubUserData source);
}
