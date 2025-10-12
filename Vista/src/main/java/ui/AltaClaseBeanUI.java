package ui;

import helper.ClaseHelper;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import mx.desarollo.entity.Clase;

import java.io.Serializable;

@Named("altaClaBeanUI") //Nombre que se usa en el xhtml
@SessionScoped
public class AltaClaseBeanUI implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String horario;
    private int cupoMaximo;
    private String maestro;

    private final ClaseHelper claseHelper = new ClaseHelper();

    public void altaClase() {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new Exception("El campo Nombre es obligatorio");
            }
            if (horario == null || horario.trim().isEmpty()) {
                throw new Exception("El campo Horario es obligatorio");
            }
            if (cupoMaximo <= 0) {
                throw new Exception("El campo Cupo máximo debe ser mayor a 0");
            }
            if (maestro == null || maestro.trim().isEmpty()) {
                throw new Exception("El campo Maestro es obligatorio");
            }

            //Se crea el objeto clase y se la pasa al helper
            Clase nuevaClase = new Clase();
            nuevaClase.setNombre(nombre);
            nuevaClase.setHorario(horario);
            nuevaClase.setCupoMaximo(cupoMaximo);
            nuevaClase.setMaestro(maestro);

            claseHelper.AltaClase(nuevaClase);

            //Mostrar exito en la interfaz
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro exitoso", "La clase fue agregada correctamente."));
            limpiarCampos();
        } catch (Exception e) {
            //Mostrar error en la interfaz
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar clase", e.getMessage()));
        }
    }

    //Esta funcion pone en blanco los campos del formulario de xhtml
    private void limpiarCampos() {
        nombre = "";
        horario = "";
        cupoMaximo = 0;
        maestro = "";
    }

    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getHorario(){
        return horario;
    }
    public void setHorario(String horario){
        this.horario = horario;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }
    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public String getMaestro() {
        return maestro;
    }
    public void setMaestro(String maestro) {
        this.maestro = maestro;
    }
}
