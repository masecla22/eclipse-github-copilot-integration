package com.github.copilot.github;

public final class GitHubSession {
    private final String token;

    public GitHubSession(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitHubSession)) {
            return false;
        }
        GitHubSession other = (GitHubSession)o;
        String this$token = this.getToken();
        String other$token = other.getToken();
        return !(this$token == null ? other$token != null : !this$token.equals(other$token));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $token = this.getToken();
        result = result * 59 + ($token == null ? 43 : $token.hashCode());
        return result;
    }

    public String toString() {
        return "GitHubSession(token=" + this.getToken() + ")";
    }
}

