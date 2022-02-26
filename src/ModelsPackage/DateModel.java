package ModelsPackage;

import java.util.ArrayList;

public class DateModel {

    private int an;
    private int luna;
    private int zi;
    private int ora;
    private int minute;
    private static final ArrayList<Integer> maxZileLuna = new ArrayList<>();

    public DateModel() {}

    public DateModel(int an, int luna, int zi, int ora, int minute) {
        this.an = an;
        this.luna = luna;
        this.zi = zi;
        this.ora = ora;
        this.minute = minute;

        maxZileLuna.add(31);
        maxZileLuna.add(28);
        maxZileLuna.add(31);
        maxZileLuna.add(30);
        maxZileLuna.add(31);
        maxZileLuna.add(30);
        maxZileLuna.add(31);
        maxZileLuna.add(31);
        maxZileLuna.add(30);
        maxZileLuna.add(31);
        maxZileLuna.add(30);
        maxZileLuna.add(31);
    }

    public int getZi() {
        return zi;
    }

    public void setZi(int zi) {
        this.zi = zi;
    }

    public int getOra() {
        return ora;
    }

    public void setOra(int ora) {
        this.ora = ora;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getAn() {
        return an;
    }

    public void setAn(int an) {
        this.an = an;
    }

    public int getLuna() {
        return luna;
    }

    public void setLuna(int luna) {
        this.luna = luna;
    }

    @Override
    public String toString() {
        return this.an + "-" + this.luna + "-" + this.zi + " " + this.ora + ":" + this.minute + ":" + "00";
    }

    public int comparareTot(DateModel date) {

        if (this.getAn() < date.getAn()) {
            return 0;
        } else if (this.getAn() == date.getAn()) {

            if (this.getLuna() < date.getLuna()) {
                return 0;
            } else if (this.getLuna() == date.getLuna()) {

                if (this.getZi() < date.getZi()) {
                    return 0;
                } else if (this.getZi() == date.getZi()) {

                    if (this.getOra() < date.getOra()) {
                        return 0;
                    } else if (this.getOra() == date.getOra()) {

                        if (this.getMinute() < date.getMinute()) {
                            return 0;
                        } else if (this.getMinute() == date.getMinute()) {
                            return 1;
                        } else {
                            return 2;
                        }

                    } else {
                        return 2;
                    }

                } else {
                    return 2;
                }

            } else {
                return 2;
            }

        } else if (this.getAn() > date.getAn()) {
            return 2;
        }
        return -1;
    }

    public int comparare(DateModel date) {
        if (this.getOra() < date.getOra()) {
            return 0;
        } else if (this.getOra() == date.getOra()) {

            if (this.getMinute() < date.getMinute()) {
                return 0;
            } else if (this.getMinute() == date.getMinute()) {
                return 1;
            } else {
                return 2;
            }

        } else {
            return 2;
        }
    }

    public DateModel adunare(int nrOre) {

        if (this.getOra() + nrOre < 24) {
            return new DateModel(this.getAn(), this.getLuna(), this.getZi(), this.getOra() + nrOre, this.getMinute());
        }else {

            int zilePlus = (this.getOra() + nrOre) / 24;
            int oreRamase = (this.getOra() + nrOre) % 24;

            if (this.getZi() +  zilePlus < maxZileLuna.get(this.getLuna()-1)) {
                return new DateModel(this.getAn(), this.getLuna(), this.getZi() + zilePlus, oreRamase, this.getMinute());
            } else {

                int lunaPlus = (this.getZi() + zilePlus) / maxZileLuna.get(this.getLuna() - 1);
                int zileRamase = (this.getZi() + zilePlus) % maxZileLuna.get(this.getLuna() - 1);

                if (this.getLuna() + lunaPlus < 12) {
                    return new DateModel(this.getAn(), this.getLuna() + lunaPlus, zileRamase, oreRamase, this.getMinute());
                }else {
                    int anPlus = (this.getLuna() + lunaPlus) / 12;
                    int luniRamase = (this.getLuna() + lunaPlus) % 12;
                    return new DateModel(this.getAn() + anPlus, luniRamase, zileRamase, oreRamase, this.getMinute());
                }
            }
        }
    }

}
