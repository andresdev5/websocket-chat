package ec.edu.espe.chatws.chatwebsocketserver.config;

import ec.edu.espe.chatws.chatwebsocketserver.dto.ChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserChatRoomDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserDto;
import ec.edu.espe.chatws.chatwebsocketserver.dto.UserPreferenceDto;
import ec.edu.espe.chatws.chatwebsocketserver.entity.ChatRoom;
import ec.edu.espe.chatws.chatwebsocketserver.entity.User;
import ec.edu.espe.chatws.chatwebsocketserver.jwt.JwtTokenFilter;
import ec.edu.espe.chatws.chatwebsocketserver.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfiguration {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.addConverter(userConverter());

        modelMapper.addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                map().getChatRoom().setOwner(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<ChatRoom, ChatRoomDto>() {
            @Override
            protected void configure() {
                map().setOwner(null);
            }
        });

        return modelMapper;
    }

    private Converter<User, UserDto> userConverter() {
        return context -> {
            User source = context.getSource();

            return UserDto.builder()
                    .id(source.getId())
                    .username(source.getUsername())
                    .role(source.getRole())
                    .accountNonExpired(source.isAccountNonExpired())
                    .accountNonExpired(source.isAccountNonExpired())
                    .accountNonLocked(source.isAccountNonLocked())
                    .enabled(source.isEnabled())
                    .credentialsNonExpired(source.isCredentialsNonExpired())
                    .chatRoom(source.getChatRoom() != null ? ChatRoomDto.builder()
                            .id(source.getChatRoom().getId())
                            .name(source.getChatRoom().getName())
                            .description(source.getChatRoom().getDescription())
                            .type(source.getChatRoom().getType())
                            .owner(UserDto.builder()
                                    .id(source.getChatRoom().getOwner().getId())
                                    .role(source.getChatRoom().getOwner().getRole())
                                    .username(source.getChatRoom().getOwner().getUsername())
                                    .preferences(UserPreferenceDto.builder()
                                            .id(source.getChatRoom().getOwner().getPreferences().getId())
                                            .about(source.getChatRoom().getOwner().getPreferences().getAbout())
                                            .avatar(source.getChatRoom().getOwner().getPreferences().getAvatar())
                                            .color(source.getChatRoom().getOwner().getPreferences().getColor())
                                            .status(source.getChatRoom().getOwner().getPreferences().getStatus())
                                            .theme(source.getChatRoom().getOwner().getPreferences().getTheme())
                                            .build())
                                    .build())
                            .build() : null)
                    .preferences(UserPreferenceDto.builder()
                            .id(source.getPreferences().getId())
                            .about(source.getPreferences().getAbout())
                            .avatar(source.getPreferences().getAvatar())
                            .color(source.getPreferences().getColor())
                            .status(source.getPreferences().getStatus())
                            .theme(source.getPreferences().getTheme())
                            .build())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/chat-rooms/create")).hasRole("ADMIN")
                        .requestMatchers(
                                "/index.html",
                                "/ws/**",
                                "/api/auth/**",
                                "/api/chat-rooms/**",
                                "/api/information/**",
                                "/api/user/**",
                                "/queue/reply/**",
                                "/topic/event"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((request, response, authException) -> response.sendError(401, "Unauthorized"));
                    ex.accessDeniedHandler((request, response, authException) -> response.sendError(403, "Forbidden"));
                })
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }
}