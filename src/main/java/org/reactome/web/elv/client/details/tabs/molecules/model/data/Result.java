package org.reactome.web.elv.client.details.tabs.molecules.model.data;

import org.reactome.web.elv.client.common.utils.Console;
import org.reactome.web.elv.client.common.utils.MapSet;
import org.reactome.web.elv.client.details.tabs.molecules.model.type.PropertyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Kerstin Hausmann <khaus@ebi.ac.uk>
 */
public class Result {
    //"grouping" for different kind of molecules
    private HashSet<Molecule> chemicals = new HashSet<Molecule>();
    private HashSet<Molecule> proteins = new HashSet<Molecule>();
    private HashSet<Molecule> sequences = new HashSet<Molecule>();
    private HashSet<Molecule> others = new HashSet<Molecule>();

    //mapping each ReferenceEntity to a PhysicalEntityID
    private MapSet<PhysicalToReferenceEntityMap, Molecule> phyEntityToRefEntitySet = new MapSet<PhysicalToReferenceEntityMap, Molecule>();

    public Result(ArrayList<Molecule> molecules){
        for(Molecule molecule : molecules){
            //SchemaClass doesn't seem to be aware of hierarchy therefore each class must be checked
            switch (molecule.getSchemaClass()){
                case ENTITY_WITH_ACCESSIONED_SEQUENCE:
                    proteins.add(molecule);
                    break;
                case REFERENCE_RNA_SEQUENCE:
                case REFERENCE_DNA_SEQUENCE:
                case REFERENCE_SEQUENCE:
                    sequences.add(molecule);
                    break;
                case SIMPLE_ENTITY:
                    chemicals.add(molecule);
                    break;
                default:
                    others.add(molecule);
            }
        }
    }

    /*getters & setters*/
    public HashSet<Molecule> getChemicals() {
        return chemicals;
    }

//    public void setChemicals(HashSet<Molecule> chemicals) {
//        this.chemicals = chemicals;
//    }

    public HashSet<Molecule> getProteins() {
        return proteins;
    }

//    public void setProteins(HashSet<Molecule> proteins) {
//        this.proteins = proteins;
//    }

    public HashSet<Molecule> getSequences() {
        return sequences;
    }

//    public void setSequences(HashSet<Molecule> sequences) {
//        this.sequences = sequences;
//    }

    public HashSet<Molecule> getOthers() {
        return others;
    }

//    public void setOthers(HashSet<Molecule> others) {
//        this.others = others;
//    }

    public MapSet<PhysicalToReferenceEntityMap, Molecule> getPhyEntityToRefEntitySet() {
        return this.phyEntityToRefEntitySet;
    }

    public void setPhyEntityToRefEntitySet(MapSet<PhysicalToReferenceEntityMap, Molecule> phyEntityToRefEntitySet) {
        this.phyEntityToRefEntitySet = phyEntityToRefEntitySet;
    }

    /*getters for sorted molecules arrays*/
    public ArrayList<Molecule> getSortedChemicals(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.chemicals);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    public ArrayList<Molecule> getSortedProteins(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.proteins);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    public ArrayList<Molecule> getSortedSequences(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.sequences);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    public ArrayList<Molecule> getSortedOthers(){
        ArrayList<ArrayList<Molecule>> split = splitHighlighted(this.others);
        ArrayList<Molecule> sortedColour = new ArrayList<Molecule>(split.get(0));
        Collections.sort(sortedColour);
        ArrayList<Molecule> sortedGrey = new ArrayList<Molecule>(split.get(1));
        Collections.sort(sortedGrey);
        sortedColour.addAll(sortedGrey);
        return sortedColour;
    }

    private ArrayList<ArrayList<Molecule>> splitHighlighted(HashSet<Molecule> molecules) {
        ArrayList<Molecule> colour = new ArrayList<Molecule>();
        ArrayList<Molecule> grey   = new ArrayList<Molecule>();

        for(Molecule molecule : molecules){
            if(molecule.isToHighlight()){
                colour.add(molecule);
            }else{
                grey.add(molecule);
            }
        }

        ArrayList<ArrayList<Molecule>> all = new ArrayList<ArrayList<Molecule>>();
        all.add(colour);
        all.add(grey);
        return all;
    }

    /*getter for total number of Molecules in Result*/
    public Integer getNumberOfMolecules() {
        Integer number = 0;
        if(chemicals != null){
            number += chemicals.size();
        }

        if(proteins != null){
            number += proteins.size();
        }

        if(sequences != null){
            number += sequences.size();
        }

        if(others != null){
            number += others.size();
        }
        return number;
    }

    /*methods for highlighting and redo highlighting*/
    public void highlight(Molecule molecule){
        HashSet<Molecule> current;
        molecule.setToHighlight(true);
        if(chemicals.contains(molecule)){
            current = chemicals;
            chemicals = iterate(molecule, current);
        }else if(proteins.contains(molecule)){
            current = proteins;
            proteins = iterate(molecule, current);
        }else if(sequences.contains(molecule)){
            current = sequences;
            sequences = iterate(molecule, current);
        }else if(others.contains(molecule)){
            current = others;
            others = iterate(molecule, current);
        }
    }

    private HashSet<Molecule> iterate(Molecule molecule, HashSet<Molecule> current){
        ArrayList<Molecule> list = new ArrayList<Molecule>(current);
        for(Molecule m : list){
            if(m.equals(molecule)){
                m.setToHighlight(true);
            }
        }
        return new HashSet<Molecule>(list);
    }

    public void highlight() {
        for(Molecule m : this.chemicals){
            m.setToHighlight(true);
        }

        for(Molecule m : this.proteins){
            m.setToHighlight(true);
        }

        for(Molecule m : this.sequences){
            m.setToHighlight(true);
        }

        for(Molecule m : this.others){
            m.setToHighlight(true);
        }
    }

    public void undoHighlighting() {
        for(Molecule m : this.chemicals){
            m.setToHighlight(false);
        }

        for(Molecule m : this.proteins){
            m.setToHighlight(false);
        }

        for(Molecule m : this.sequences){
            m.setToHighlight(false);
        }

        for(Molecule m : this.others){
            m.setToHighlight(false);
        }
    }

    public int numHighlight(PropertyType category) {
        int numHighlight = -1;
        switch (category){
            case CHEMICAL_COMPOUNDS:
                numHighlight = numHighlight(this.chemicals);
                break;
            case PROTEINS:
                numHighlight = numHighlight(proteins);
                break;
            case SEQUENCES:
                numHighlight = numHighlight(sequences);
                break;
            case OTHERS:
                numHighlight = numHighlight(others);
                break;
            default:
                numHighlight = -1;
                Console.error("There was an additional molecules category in class Result -> numHighlight.");
        }
        return numHighlight;
    }

    private int numHighlight(HashSet<Molecule> toHighlight){
        int numHighlight = 0;
        for(Molecule molecule : toHighlight){
            if(molecule.isToHighlight()){
                ++ numHighlight;
            }
        }
        return numHighlight;
    }
}
