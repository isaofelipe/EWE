package com.example.isaofelipemorigaki.ewe.firebase;

public class Contribuicao {
    private String id;

    private String namespace;

    private String instance;

    private String local;

    private String mensagemFixa;

    private String mensagemTemporaria;

    private int idBeacon;

    private String idColaborador;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getMensagemFixa() {
        return mensagemFixa;
    }

    public void setMensagemFixa(String mensagemFixa) {
        this.mensagemFixa = mensagemFixa;
    }

    public String getMensagemTemporaria() {
        return mensagemTemporaria;
    }

    public void setMensagemTemporaria(String mensagemTemporaria) {
        this.mensagemTemporaria = mensagemTemporaria;
    }

    public int getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(int idBeacon) {
        this.idBeacon = idBeacon;
    }

    public String getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(String idColaborador) {
        this.idColaborador = idColaborador;
    }

    public Contribuicao(String id, String namespace, String instance, String local, String mensagemFixa, String mensagemTemporaria, int idBeacon, String idColaborador) {
        this.id = id;
        this.namespace = namespace;
        this.instance = instance;
        this.local = local;
        this.mensagemFixa = mensagemFixa;
        this.mensagemTemporaria = mensagemTemporaria;
        this.idBeacon = idBeacon;
        this.idColaborador = idColaborador;
    }

    public Contribuicao() {
    }
}
