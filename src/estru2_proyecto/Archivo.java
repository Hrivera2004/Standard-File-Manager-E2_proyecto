/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estru2_proyecto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Archivo {

    public File FileRegistros = null;
    private String filename = "";
    private Metadata metadata; //500 bytes
    private long latest_modified = -1;

    public Archivo() {
    }

    public Archivo(File FileRegistros, String filename, Metadata metadata, long latest_modified) {
        this.FileRegistros = FileRegistros;
        this.filename = filename;
        this.metadata = metadata;
        this.latest_modified = latest_modified;
    }

    public long getLatest_modified() {
        return latest_modified;
    }

    public void setLatest_modified(long latest_modified) {
        this.latest_modified = latest_modified;
    }

    public File getFileRegistros() {
        return FileRegistros;
    }

    public void setFileRegistros(File FileRegistros) {
        this.FileRegistros = FileRegistros;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public boolean create_file(String name) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = chooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            File file = new File(selectedDirectory.getAbsolutePath() + "/" + name + ".txt");
            if (file.exists()) {
                JOptionPane.showMessageDialog(null, "El archivo ya existe.");
                return false;
            }
            try {
                file.createNewFile();
                open_file(file);
                JOptionPane.showMessageDialog(null, "El archivo se a creado.");
                return true;
            } catch (IOException io) {
                JOptionPane.showMessageDialog(null, "Error al crear el archivo");
                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, "No se seleciono ningun directorio");
            return false;
        }
    }

    public boolean open_file(File selected) throws IOException {
        //modificar
        FileRegistros = selected;
        int lastIndex = selected.getName().lastIndexOf('.');
        filename = selected.getName().substring(0, lastIndex);

        try (RandomAccessFile file = new RandomAccessFile(FileRegistros, "rw")) {
            if (file.length() >= 500) {
                return LoadMetaData();
            }
        }
        return false;
    }
    //modificar

    public void close_file() throws IOException {//guarda y cierra el archivo
        try {
            if (metadata != null) {
                addMetadataToFile();
            }
            FileRegistros = null;
            filename = "";
            metadata = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error : No se pudo cerrar el archivo");
        }

    }

    public void addMetadataToFile() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FileRegistros, "rw")) {
            // Convertir la metadata en una cadena y asegurarse de que tenga exactamente 500 bytes
            String metadataString = metadata.toString();
            StringBuilder sb = new StringBuilder(metadataString);

            // Rellenar con espacios si es más corto
            while (sb.length() < 499) {
                sb.append(" ");
            }
            metadataString = sb.toString() + "\n";

            // Sobrescribir los primeros 500 bytes del archivo
            file.seek(0);
            file.writeBytes(metadataString);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: No se pudo escribir los metadatos.");
            e.printStackTrace();
            throw e;
        }
    }

    public void addMetadataToFile_modify() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(FileRegistros, "rw")) {
            // Convertir la metadata en una cadena y asegurarse de que tenga exactamente 500 bytes
            String metadataString = metadata.toString();
            StringBuilder sb = new StringBuilder(metadataString);

            // Rellenar con espacios si es más corto
            while (sb.length() < 499) {
                sb.append(" ");
            }
            metadataString = sb.toString();

            // Sobrescribir los primeros 500 bytes del archivo
            file.seek(0);
            file.writeBytes(metadataString);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: No se pudo escribir los metadatos.");
            e.printStackTrace();
            throw e;
        }
    }

    public boolean LoadMetaData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FileRegistros))) {
            // Leer la primera línea de los metadatos y limpiar espacios innecesarios
            String line = reader.readLine();

            if (line == null || line.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Metadatos vacíos o archivo no válido.");
                return false;
            }
            line = line.trim();

            // Procesar la línea como metadatos
            String[] temp_metadata = line.split(";");

            if (temp_metadata.length < 3) {
                return false;
            }

            String[] temp_campos = temp_metadata[1].split("\\|");

            ArrayList<Campo> campos = new ArrayList<>();
            for (String campo : temp_campos) {
                String[] split = campo.split("-");
                campos.add(new Campo(
                        split[0].trim(),
                        Integer.parseInt(split[1].trim()),
                        Integer.parseInt(split[2].trim()),
                        split[3].trim().equals("1"),
                        split[4].trim().equals("1")
                ));
            }

            metadata = new Metadata(
                    ((temp_metadata[0].trim().isEmpty()) ? -1 : Integer.parseInt(temp_metadata[0].trim())),
                    campos,
                    ((temp_metadata[2].trim().isEmpty()) ? -1 : Integer.parseInt(temp_metadata[2].trim())),
                    ((temp_metadata[3].trim().isEmpty()) ? -1 : Integer.parseInt(temp_metadata[3].trim())),
                    ((temp_metadata[4].trim().isEmpty()) ? -1 : Integer.parseInt(temp_metadata[4].trim()))
            );
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los metadatos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //hacer que verifique que no existe el registro al momento de introducir
    //No se crea ni se carga nada de arraylist todo se hace directamene en el archivo
    public void introducirRegistro(Registro registro) {
        if (metadata == null) {
            JOptionPane.showMessageDialog(null, "Antes de introducir registros, prepare el archivo.");
            return;
        }
        int next;
        try (RandomAccessFile file = new RandomAccessFile(FileRegistros, "rw")) {

            long offset = 500; // Start after metadata
            if (metadata.getRRN_headAvail() != -1) {
                // Write to the available list position
                offset += metadata.getRRN_headAvail() * 256;
                //Cambiar la cabeza del avail list
                Registro head_avail = LoadRegistro(metadata.getRRN_headAvail());
                next = head_avail.getRRN_next();
                metadata.setRRN_headAvail(next);
                latest_modified = head_avail.getRRN();
            } else {
                // Append to the end of the file
                offset = file.length();
                latest_modified = cant_Registros();
            }

            StringBuilder sb = new StringBuilder(registro.toString());
            while (sb.length() < 255) {
                sb.append(" ");  // Append spaces until length reaches 255
            }
            String registrostring = sb.toString() + "\n";

            file.seek(offset);
            file.writeBytes(registrostring);
            file.close();
            addMetadataToFile();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: El archivo no se encontró.");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: No se pudo escribir en el archivo.");
            e.printStackTrace();
        }
    }

    // almomento de leer usar \\| porque | es char especial
    public Registro LoadRegistro(long RRN) {
        try (RandomAccessFile file = new RandomAccessFile(FileRegistros, "rw")) {
            long offset = 500; // Start after metadata
            offset += 256 * RRN;

            file.seek(offset);
            byte[] bytes = new byte[256];
            file.read(bytes);
            file.close();

            String line = new String(bytes).trim();
            String[] split1 = line.split("\\|");
            String[] split2 = split1[0].split(";");
            ArrayList<Object> data = new ArrayList<>();
            for (int i = 0; i < split2.length; i++) {
                data.add(split2[i]);
            }
            Registro registro = new Registro(data, (!(split1[1].trim().isEmpty())), ((split1[2].trim().isEmpty()) ? -1 : Integer.parseInt(split1[2])));
            return registro;

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: El archivo no se encontró.");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: No se pudo leer el regisro en el archivo.");
            e.printStackTrace();
        }
        return null;
    }

    public long cant_Registros() {
        long record_ammount;
        try (RandomAccessFile file = new RandomAccessFile(FileRegistros, "rw")) {
            if ((file.length() - 500) >= 0) {
                record_ammount = (file.length() - 500) / 256;
                return record_ammount;
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: El archivo no se encontró.");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: No se pudo leer el regisro en el archivo.");
            e.printStackTrace();
        }
        return -1;
    }

    public Registro buscarRegistroConArbol(Object claveBusqueda, BTree arbol) {
        // Buscar la llave en el árbol B
        Llave llaveEncontrada = null;
        BTreeNode nodoEncontrado = arbol.search(claveBusqueda);
        if (nodoEncontrado != null) {
            // Si encontramos el nodo, buscar la clave dentro del nodo
            int posicion = nodoEncontrado.binarySearch(claveBusqueda);
            if (posicion >= 0) {
                llaveEncontrada = nodoEncontrado.getKeys()[posicion];
            }
        }

        if (llaveEncontrada != null) {
            // Si la llave fue encontrada, cargar el registro desde el archivo
            return LoadRegistro(llaveEncontrada.getRRN());
        } else {
            // Si la llave no está en el árbol, retornamos null
            return null;
        }
    }

    public boolean borrarRegistro(long RRN) {
        try (RandomAccessFile file = new RandomAccessFile(FileRegistros, "rw")) {
            long offset = 500 + RRN * 256; // Calcular posición del registro
            file.seek(offset);
            byte[] bytes = new byte[256];
            file.read(bytes);

            String line = new String(bytes).trim();
            String[] split = line.split("\\|");
            // Verificar que el registro no esté ya marcado como borrado
            if (!Boolean.parseBoolean(split[1])) {
                // Marcar como borrado y apuntar al siguiente registro disponible
                StringBuilder sb = new StringBuilder(split[0] + "|*|" + ((metadata.getRRN_headAvail() == -1) ? " " : metadata.getRRN_headAvail()) + "|");

                // Asegurar que el registro ocupe exactamente 256 bytes
                while (sb.length() < 255) {
                    sb.append(" ");
                }
                sb.append("\n"); // Asegurar que haya un salto de línea al final

                // Sobrescribir el registro con los datos actualizados
                file.seek(0);
                file.seek(offset);
                file.writeBytes(sb.toString());

                // Actualizar la metadata para reflejar el nuevo head del avail list
                metadata.setRRN_headAvail(RRN);
                addMetadataToFile_modify();
                return true;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al borrar el registro.");
            e.printStackTrace();
        }
        return false; // Si algo falla
    }

    public boolean borrarRegistroConArbol(Comparable clave, BTree arbol) {
        BTreeNode nodoEncontrado = arbol.search(clave);

        if (nodoEncontrado != null) {
            // Buscar la posición de la clave dentro del nodo
            int posicion = nodoEncontrado.binarySearch(clave);
            if (posicion >= 0) {
                Llave llave = nodoEncontrado.getKeys()[posicion];

                // Eliminar el registro del archivo utilizando el RRN de la llave
                boolean borradoExitoso = borrarRegistro(llave.getRRN());
                if (borradoExitoso) {
                    arbol.delete(clave);
                    return true;
                }
            }
        }
        return false; // Si no se encuentra la clave
    }

    public boolean modificarRegistro(int RRN, ArrayList<Object> nuevosDatos) {
        if (metadata == null) {
            JOptionPane.showMessageDialog(null, "Antes de modificar registros, prepare el archivo.");
            return false;
        }

        try (RandomAccessFile file = new RandomAccessFile(FileRegistros, "rw")) {
            long offset = 500 + RRN * 256; // Calcular posición del registro
            file.seek(offset);

            byte[] bytes = new byte[256];
            file.read(bytes);
            String line = new String(bytes).trim();
            String[] split = line.split("\\|");

            if (Boolean.parseBoolean(split[1])) { // Verificar si está marcado como eliminado
                JOptionPane.showMessageDialog(null, "Error: El registro está eliminado y no puede ser modificado.");
                return false;
            }

            // Validar los nuevos datos según los tipos de campos
            ArrayList<Campo> campos = metadata.getCampos();
            for (int i = 0; i < nuevosDatos.size(); i++) {
                Campo campo = campos.get(i);
                Object valor = nuevosDatos.get(i);

                switch (campo.getTipo()) {
                    case 0: // Boolean
                        if (!(valor instanceof Boolean)) {
                            JOptionPane.showMessageDialog(null, "Error: El campo " + campo.getNombre_campo() + " debe ser un valor booleano.");
                            return false;
                        }
                        break;
                    case 1: // Entero
                        if (!(valor instanceof Integer)) {
                            JOptionPane.showMessageDialog(null, "Error: El campo " + campo.getNombre_campo() + " debe ser un entero.");
                            return false;
                        }
                        break;
                    case 2: // Flotante
                        if (!(valor instanceof Float)) {
                            JOptionPane.showMessageDialog(null, "Error: El campo " + campo.getNombre_campo() + " debe ser un número flotante.");
                            return false;
                        }
                        break;
                    case 3: // Cadena
                        if (!(valor instanceof String)) {
                            JOptionPane.showMessageDialog(null, "Error: El campo " + campo.getNombre_campo() + " debe ser una cadena de texto.");
                            return false;
                        }
                        if (((String) valor).length() > campo.getLongitud()) {
                            JOptionPane.showMessageDialog(null, "Error: El campo " + campo.getNombre_campo() + " excede la longitud máxima permitida.");
                            return false;
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Error: Tipo de campo no soportado.");
                        return false;
                }
            }

            // Preparar el nuevo registro para escribir
            StringBuilder sb = new StringBuilder();
            for (Object dato : nuevosDatos) {
                sb.append(dato.toString()).append(";");
            }
            sb.append("| |").append(split[2]).append("|"); // Mantener el RRN_next

            while (sb.length() < 255) {
                sb.append(" "); // Completar hasta 255 caracteres
            }
            sb.append("\n");

            // Sobreescribir el registro en el archivo
            file.seek(offset);
            file.writeBytes(sb.toString());
            return true;

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: El archivo no se encontró.");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: No se pudo modificar el registro.");
            e.printStackTrace();
        }
        return false;
    }

    public void exportToXMLSchema(String filePath, String name) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
            String schema = new File(filePath).getParent() + File.separator + name + ".xsd";
            
            generateXSD(schema,name, metadata.getCampos()); // Use schema to ensure the path is correct

            StringBuilder guardar = new StringBuilder();
            guardar.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
            guardar.append("<xml>\n");

            guardar.append("    <!-- XML Schema Definition -->\n");
            guardar.append("    <schema xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
            guardar.append("            xsi:noNamespaceSchemaLocation=\"").append(schema).append("\">\n");
            guardar.append("    </schema>\n");

            guardar.append("<registros>\n");

            long totalRegistros = cant_Registros();

            for (int k = 0; k < totalRegistros; k++) {
                Registro registro = LoadRegistro(k);
                if (registro != null && !registro.isBorrado()) {
                    guardar.append("    <registro>\n");
                    for (int s = 0; s < metadata.getCampos().size(); s++) {
                        Campo campo = metadata.getCampos().get(s);
                        String fieldName = campo.getNombre_campo().replace(" ", "");
                        Object fieldValue = registro.getData().get(s);
                        guardar.append("        <").append(fieldName).append(">")
                                .append(fieldValue != null ? fieldValue.toString() : "")
                                .append("</").append(fieldName).append(">\n");
                    }
                    guardar.append("    </registro>\n");
                }
            }
            guardar.append("</registros>\n");
            guardar.append("</xml>");
            writer.write(guardar.toString());
            writer.close();

            JOptionPane.showMessageDialog(null, "Archivo exportado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al exportar el archivo");
        }
    }

    public void generateXSD(String path,String nanme, ArrayList<Campo> campos) throws IOException {
        String schema = new File(path).getParent() + File.separator + nanme + ".xsd";
        try (FileWriter writer = new FileWriter(new File(schema))) {
            StringBuilder xsd = new StringBuilder();
            xsd.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xsd.append("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\">\n");

            xsd.append("    <xs:element name=\"registros\">\n");
            xsd.append("        <xs:complexType>\n");
            xsd.append("            <xs:sequence>\n");
            xsd.append("                <xs:element name=\"registro\" maxOccurs=\"unbounded\">\n");
            xsd.append("                    <xs:complexType>\n");
            xsd.append("                        <xs:sequence>\n");
            for (Campo campo : campos) {
                String fieldType = getXSDType(campo.getTipo());
                xsd.append("                            <xs:element name=\"")
                        .append(campo.getNombre_campo())
                        .append("\" type=\"")
                        .append(fieldType)
                        .append("\" minOccurs=\"0\" maxOccurs=\"1\"");
                if (campo.getTipo() == 3 || campo.getTipo() == 4) {
                    xsd.append(">\n");
                    xsd.append("                                <xs:simpleType>\n");
                    xsd.append("                                    <xs:restriction base=\"xs:").append(fieldType).append("\">\n");
                    xsd.append("                                        <xs:maxLength value=\"").append(campo.getLongitud()).append("\"/>\n");
                    xsd.append("                                    </xs:restriction>\n");
                    xsd.append("                                </xs:simpleType>\n");
                    xsd.append("                            </xs:element>\n");
                } else {
                    xsd.append("/>\n");
                }
            }
            xsd.append("                        </xs:sequence>\n");
            xsd.append("                    </xs:complexType>\n");
            xsd.append("                </xs:element>\n");
            xsd.append("            </xs:sequence>\n");
            xsd.append("        </xs:complexType>\n");
            xsd.append("    </xs:element>\n");
            xsd.append("</xs:schema>");
            writer.write(xsd.toString());
        }
    }

    private static String getXSDType(int tipo) {
        switch (tipo) {
            case 0:
                return "xs:boolean";
            case 1:
                return "xs:int";
            case 2:
                return "xs:float";
            default:
                return "xs:string";
        }
    }
}
