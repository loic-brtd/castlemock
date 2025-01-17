/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.model.core.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * The User DTO class is a DTO (Data transfer object) for the user class
 * @author Karl Dahlgren
 * @since 1.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonDeserialize(builder = User.Builder.class)
public class User {

    @XmlElement
    private final String id;

    @XmlElement
    private final String username;

    @XmlElement
    private final String password;

    @XmlElement
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String email;

    @XmlElement
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String fullName;

    @XmlElement
    private final Date updated;

    @XmlElement
    private final Date created;

    @XmlElement
    private final Status status;

    @XmlElement
    private final Role role;

    private User(final Builder builder){
        this.id = Objects.requireNonNull(builder.id, "id");
        this.username = Objects.requireNonNull(builder.username, "username");
        this.password = Objects.requireNonNull(builder.password, "password");
        this.email = builder.email;
        this.fullName = builder.fullName;
        this.updated = Objects.requireNonNull(builder.updated, "updated");
        this.created = Objects.requireNonNull(builder.created, "created");
        this.status = Objects.requireNonNull(builder.status, "status");
        this.role = Objects.requireNonNull(builder.role, "role");
    }

    /**
     * Get the user id
     * @return User id
     */
    public String getId() {
        return id;
    }

    /**
     * Get the user username
     * @return User username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the user password
     * @return Returns the user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get user email
     * @return Returns user email
     */
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    /**
     * Return the timestamp for when the user was updated
     * @return Updated timestamp
     */
    public Date getUpdated() {
        return updated;
    }

    public Optional<String> getFullName() {
        return Optional.ofNullable(fullName);
    }

    /**
     * Returns the timestamp of when the user was created
     * @return Created timestamp
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Get the current status of user
     * @return User status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns the users current role
     * @return User role
     */
    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username)
                && Objects.equals(password, user.password) && Objects.equals(email, user.email)
                && Objects.equals(fullName, user.fullName) && Objects.equals(updated, user.updated)
                && Objects.equals(created, user.created) && status == user.status && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, fullName, updated, created, status, role);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .email(this.email)
                .username(this.username)
                .updated(this.updated)
                .created(this.created)
                .password(this.password)
                .status(this.status)
                .fullName(this.fullName)
                .role(this.role);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private String id;
        private String username;
        private String password;
        private String email;
        private String fullName;
        private Date updated;
        private Date created;
        private Status status;
        private Role role;

        private Builder() {
        }

        public Builder id(final String id) {
            this.id = id;
            return this;
        }

        public Builder username(final String username) {
            this.username = username;
            return this;
        }

        public Builder password(final String password) {
            this.password = password;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder fullName(final String fullName) {
            this.fullName = fullName;
            return this;
        }


        public Builder updated(final Date updated) {
            this.updated = updated;
            return this;
        }

        public Builder created(final Date created) {
            this.created = created;
            return this;
        }

        public Builder status(final Status status) {
            this.status = status;
            return this;
        }

        public Builder role(final Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
