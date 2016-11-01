package ca.csf.tp2.Modele;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Alicia Lamontagne
 */
public class EtudiantTest extends TestCase {

    private Etudiant etudiant;

    @Before
    public void before() {
        etudiant = new Etudiant("Etudiant1", "123456789000");
    }

    @Test
    public void testConstructionParametresNull() throws Exception {
        etudiant = new Etudiant(null, null);
        assertEquals("defaut", etudiant.getCode());
        assertEquals("defaut", etudiant.getNom());
    }

    @Test
    public void testConstructionParametresVides() throws Exception {
        etudiant = new Etudiant("", "");
        assertEquals("defaut", etudiant.getCode());
        assertEquals("defaut", etudiant.getNom());

        etudiant = new Etudiant("    ", "            ");
        assertEquals("defaut", etudiant.getCode());
        assertEquals("defaut", etudiant.getNom());
    }

    @Test
    public void testConstructionCodeInvalide() throws Exception {
        etudiant = new Etudiant("Etudiant1", "123456");
        assertEquals("defaut", etudiant.getCode());
    }

    @Test
    public void testSetCodeValide() throws Exception {
        etudiant = new Etudiant("Etudiant1", "123456789000");
        etudiant.setCode("555555555a5a");
        assertEquals("555555555a5a", etudiant.getCode());
    }

    @Test
    public void testSetCodeInvalide() throws Exception {
        etudiant = new Etudiant("Etudiant1", "123456789000");
        etudiant.setCode("");
        assertEquals("123456789000", etudiant.getCode());
        etudiant.setCode(" ");
        assertEquals("123456789000", etudiant.getCode());
        etudiant.setCode("            ");
        assertEquals("123456789000", etudiant.getCode());
        etudiant.setCode(null);
        assertEquals("123456789000", etudiant.getCode());
    }

    @Test
    public void testSetNomValide() throws Exception {
        etudiant = new Etudiant("Etudiant1", "123456789000");
        etudiant.setNom("Rainbow Dash");
        assertEquals("Rainbow Dash", etudiant.getNom());
    }

    @Test
    public void testSetNomInvalide() throws Exception {
        etudiant = new Etudiant("Etudiant1", "123456789000");
        etudiant.setNom(null);
        assertEquals("Etudiant1", etudiant.getNom());
        etudiant.setNom("");
        assertEquals("Etudiant1", etudiant.getNom());
        etudiant.setNom("     ");
        assertEquals("Etudiant1", etudiant.getNom());
    }


}