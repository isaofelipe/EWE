package com.example.isaofelipemorigaki.ewe.firebase;

public class Beacons {
    private int id;

    private String namespace;

    private String instance;

    private String local;

    private String mensagemFixa;

    private String mensagemTemporaria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Beacons() {
    }

    public Beacons(String instance) {
        this.instance = instance;
    }

    public Beacons(String instance, String local, String mensagemFixa, String mensagemTemporaria) {
        this.instance = instance;
        this.local = local;
        this.mensagemFixa = mensagemFixa;
        this.mensagemTemporaria = mensagemTemporaria;
    }

    @Override
    public boolean equals(Object obj) {
        return (this.instance.equals(((Beacons)obj).getInstance()));
    }
}
