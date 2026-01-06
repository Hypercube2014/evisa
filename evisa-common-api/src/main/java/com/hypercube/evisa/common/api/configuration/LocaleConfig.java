package com.hypercube.evisa.common.api.configuration;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author sivasreenivas
 *
 */
@Configuration
public class LocaleConfig {

	/**
	 * 
	 */
	public static final String UTF_8 = "UTF-8";

	/**
	 * @param messageSource
	 * @return
	 */
	@Bean
	public MessageSourceAccessor getMessageSourceAccessor(final MessageSource messageSource) {
		return new MessageSourceAccessor(messageSource, Locale.US);
	}

	/**
	 * @return
	 */
	@Bean(name = "messageSource")
	public static ReloadableResourceBundleMessageSource getMessageSource() {
		final ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setDefaultEncoding(UTF_8);
		res.addBasenames("classpath:messages");
		return res;
	}

	/**
	 * @return
	 */
	@Bean(name = "validationSource")
	public static ReloadableResourceBundleMessageSource getValidationSource() {
		final ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setDefaultEncoding(UTF_8);
		res.addBasenames("classpath:validation");
		return res;
	}

	/**
	 * @return
	 */
	@Bean(name = "authenticationSource")
	public static ReloadableResourceBundleMessageSource getAuthenticationSource() {
		final ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setDefaultEncoding(UTF_8);
		res.addBasenames("classpath:authentication");
		return res;
	}

	/**
	 * @param key
	 * @param locale
	 * @param source
	 * @return
	 */
	public static String getResourceValue(String key, Object[] args, String locale, String source) {
		if (source == null) {
			return getMessageSource().getMessage(key, args, new Locale(locale));
		} else if ("VAL".equals(source)) {
			return getValidationSource().getMessage(key, args, new Locale(locale));
		} else if ("AUTH".equals(source)) {
			return getAuthenticationSource().getMessage(key, args, new Locale(locale));
		} else {
			return null;
		}

	}
}
