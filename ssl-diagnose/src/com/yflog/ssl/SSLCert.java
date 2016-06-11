package com.yflog.ssl;

import sun.security.x509.X500Name;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vincent on 4/22/16.
 */
public class SSLCert {
//    public final String

    private final X500Name _subJectX500Name;
    private final X500Name _issuerX500Name;
    private final List<String> _subjectAlternativeNames;
    private final Date _notBefore;
    private final Date _notAfter;
    private final PublicKey _publicKey;
    private final String _signatureAlgo;
    private final int _version;

    public SSLCert(Builder builder) {
        _subJectX500Name = builder._subJectX500Name;
        _issuerX500Name = builder._issuerX500Name;
        _subjectAlternativeNames = builder._subjectAlternativeNames;
        _notAfter = builder._notAfter;
        _notBefore = builder._notBefore;
        _publicKey = builder._publicKey;
        _signatureAlgo = builder._signatureAlgo;
        _version = builder._version;
    }

    public String getSubjectCN() throws IOException {
        return _subJectX500Name == null ? null : _subJectX500Name.getCommonName();
    }

    public String getSubjectCountry() throws IOException {
        return _subJectX500Name == null ? null : _subJectX500Name.getCountry();
    }

    public String getSubjectState() throws IOException {
        return _subJectX500Name == null ? null : _subJectX500Name.getState();
    }

    public String getSubjectLocation() throws IOException {
        return  _subJectX500Name == null ? null :_subJectX500Name.getLocality();
    }

    public String getSubjectOrganization() throws IOException {
        return _subJectX500Name == null ? null : _subJectX500Name.getOrganization();
    }

    public String getIssuerCN() throws IOException {
        return  _issuerX500Name == null ? null :_issuerX500Name.getCommonName();
    }

    public String getIssuerCountry() throws IOException {
        return _issuerX500Name == null ? null : _issuerX500Name.getCommonName();
    }

    public String getIssuerOrganization() throws IOException {
        return _issuerX500Name == null ? null : _issuerX500Name.getOrganization();
    }

    public String getIssuerOrganizationUnit() throws IOException {
        return _issuerX500Name == null ? null : _issuerX500Name.getOrganizationalUnit();
    }

    public int getVersion() {
        return _version;
    }

    public List<String> getSubjectAlternativeNames() {
        return new ArrayList<>(_subjectAlternativeNames);
    }

    public static class Builder {
        private X500Name _subJectX500Name;
        private X500Name _issuerX500Name;
        private List<String> _subjectAlternativeNames;
        public Date _notBefore;
        public Date _notAfter;
        public PublicKey _publicKey;
        public String _signatureAlgo;
        public int _version;

        public Builder version(int version) {
            _version = version;
            return this;
        }

        public Builder subJectX500Name(X500Name name) {
            _subJectX500Name = name;
            return this;
        }

        public Builder issuerX500Name(X500Name name) {
            _issuerX500Name = name;
            return this;
        }

        public Builder subJectAlternames(List<String> alternames) {
            this._subjectAlternativeNames = alternames;
            return this;
        }

        public Builder notBefore(Date date) {
            _notBefore = date;
            return this;
        }

        public Builder notAfter(Date date) {
            _notAfter = date;
            return this;
        }

        public Builder publicKey(PublicKey publicKey) {
            _publicKey = publicKey;
            return this;
        }

        public Builder sigAlgName(String name) {
            this._signatureAlgo = name;
            return this;
        }

        public SSLCert build() {
            return new SSLCert(this);
        }


    }
}
