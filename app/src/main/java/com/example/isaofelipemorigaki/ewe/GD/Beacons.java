package com.example.isaofelipemorigaki.ewe.GD;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Beacons {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "namespace")
    private String namespace;

    @ColumnInfo(name = "instance")
    private String instance;

    @ColumnInfo(name = "local")
    private String local;

    @ColumnInfo(name = "mensagemFixa")
    private String mensagemFixa;

    @ColumnInfo(name = "mensagemTemporaria")
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

    public Beacons(String instance, String local, String mensagemFixa, String mensagemTemporaria) {
        this.instance = instance;
        this.local = local;
        this.mensagemFixa = mensagemFixa;
        this.mensagemTemporaria = mensagemTemporaria;
    }
}
