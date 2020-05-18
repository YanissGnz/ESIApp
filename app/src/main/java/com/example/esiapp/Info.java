package com.example.esiapp;

public class Info
{

    public Info(){}

    public Info(String gender, String grad, String groupe)
    {
        Gender = gender;
        Grad = grad;
        Groupe = groupe;
    }

    String Gender ;
    String Grad ;
    String Groupe ;

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getGrad() {
        return Grad;
    }

    public void setGrad(String grad) {
        Grad = grad;
    }

    public String getGroupe() {
        return Groupe;
    }

    public void setGroupe(String groupe) {
        Groupe = groupe;
    }
}
