/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.emby.add.other.folder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author robert
 */
public class Checker {
    
    private static List<String> endings = null;
    private static List<String> foldersWithDataAndSubfolders = new ArrayList<String>();
    private static List<String> foldersWithDataAndSubfolderData = new ArrayList<String>();
    private static boolean dryRun = true;
    
    public static void main(String[] args) {
        
        try {
        
            if (args.length != 3) {
                System.out.println("usage: java -jar emby-add-other-folder.jar <foldername> <endings> true/false");
                System.exit(0);
            }

            endings = Arrays.asList(args[1].toUpperCase().split(","));  
            System.out.println("Endings: "+endings);
           
            dryRun = Boolean.valueOf(args[2]);
            System.out.println("DryRun: "+dryRun);

            //scan
            File f = new File(args[0]);
            System.out.println("Folder: "+f.getCanonicalPath());
            checkFolder(f);
            
            //create other folders
            for (String folderStr : foldersWithDataAndSubfolderData) {                
                if (!dryRun) {
                    System.out.println("creating other folder for: "+folderStr);
                    File folder = new File(folderStr);
                    //create other folder
                    File otherFolder = new File(folderStr+"/other");
                    otherFolder.mkdir();
                    //move files
                    for (File sub : folder.listFiles()) {
                        if (sub.isFile()) {
                            for (String ending : endings) {
                                if (sub.getName().toUpperCase().endsWith(ending)) {
                                    //move
                                    if(sub.renameTo(new File(otherFolder.getCanonicalPath()+"/"+sub.getName()))) { 
                                        // if file copied successfully then delete the original file 
                                        sub.delete();
                                        //System.out.println("File moved successfully"); 
                                    } else { 
                                        //System.out.println("Failed to move the file"); 
                                    } 
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("DRYRUN creating other folder for: "+folderStr);
                }
            }

            System.out.println("DONE");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void checkFolder(File folder) throws IOException {
        
        String foldername = folder.getCanonicalPath();
        
        if (folder.isDirectory()) {
            
            boolean containsData = false;
            boolean containsFolder = false;
            
            for (File sub : folder.listFiles()) {
                if (sub.isFile()) {
                    for (String ending : endings) {
                        if (sub.getName().toUpperCase().endsWith(ending)) {
                            containsData = true;
                        }
                    }
                }
                if (sub.isDirectory()) {
                    containsFolder = true;                    
                }              
            }           
            
            //check stored ones if there is a parent element with data
            if (containsData) {
                for (String stored : foldersWithDataAndSubfolders) {
                    if (foldername.startsWith(stored)) {
                        if (!foldersWithDataAndSubfolderData.contains(stored)) {
                            foldersWithDataAndSubfolderData.add(stored);
                            //System.out.println("data and folders with data: "+stored);
                        }
                    }
                }
            }
            
            //store this one
            if (containsData && containsFolder) {
                foldersWithDataAndSubfolders.add(foldername);
                //System.out.println("data and folders: "+foldername);
            }
            
            //recursion
            for (File sub : folder.listFiles()) {                
                if (sub.isDirectory()) {
                    checkFolder(sub);                   
                }              
            }
        }       
        
    }
    
}
