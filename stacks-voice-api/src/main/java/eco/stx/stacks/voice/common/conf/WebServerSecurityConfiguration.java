package eco.stx.stacks.voice.common.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${stacks-voice.security.enable-csrf}")
    private boolean csrfEnabled;

    @Override
	protected void configure(HttpSecurity http) throws Exception {
        if(!csrfEnabled)
        {
          http.csrf().disable();
        }
    	http.cors().and()
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
					.antMatchers("/**").permitAll()
					.anyRequest().authenticated()
			);
	}
}