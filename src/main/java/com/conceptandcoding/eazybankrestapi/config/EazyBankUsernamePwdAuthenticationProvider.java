package com.conceptandcoding.eazybankrestapi.config;

import com.conceptandcoding.eazybankrestapi.entity.Authority;
import com.conceptandcoding.eazybankrestapi.entity.Customer;
import com.conceptandcoding.eazybankrestapi.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class EazyBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private CustomerRepository customerRepository;

    private PasswordEncoder passwordEncoder;

    public EazyBankUsernamePwdAuthenticationProvider(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String endUserPwd = authentication.getCredentials().toString(); // plain-text

        List<Customer> customers = customerRepository.findByEmail(username);

        if (!customers.isEmpty()) {

            Customer customer = customers.get(0);

            String dbPwd = customer.getPwd(); // hash value
            //String role = customer.getRole();

            if (passwordEncoder.matches(endUserPwd, dbPwd)) { // compare two passwords

                //List<GrantedAuthority> authorities = new ArrayList<>();
                //authorities.add(new SimpleGrantedAuthority(role));

                return new UsernamePasswordAuthenticationToken(
                        username,
                        endUserPwd,
                        getGrantedAuthorities(customer.getAuthorities())
                );

            } else {
                throw new BadCredentialsException("Invalid password!");
            }
        } else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    // helper method for get GrantedAuthorities
    private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }

        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
