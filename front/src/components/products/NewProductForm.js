import React, { useState } from 'react';
import { Form, Button, Row, Col, Card } from 'react-bootstrap';
import { ref, uploadBytes, getDownloadURL } from "firebase/storage";
import { storage } from "../../firebaseConfig";

import InputField from '../common/InputField';

export default function NewProductForm({onSubmit}) {
  const [productName, setProductName] = useState('');
  const [newAttribute, setNewAttribute] = useState("");
  const [attributes, setAttributes] = useState([]);
  const [variants, setVariants] = useState([]);
  const [formErrors, setFormErrors] = useState({});
  const [productPhoto, setProductPhoto] = useState(null);
  const [productPhotoURL, setProductPhotoURL] = useState("");
  const [uploading, setUploading] = useState(false);
  const [uploadError, setUploadError] = useState("");

  const handleProductNameChange = (e) => setProductName(e.target.value);
  const handleNewAttributeChange = (e) => setNewAttribute(e.target.value);

  const handleVariantChange = (variantIndex, attributeName, event) => {
    const newVariants = [...variants];
    newVariants[variantIndex][attributeName] = event.target.value;
    setVariants(newVariants);
  };

  const addAttribute = () => {
    if (newAttribute.trim() !== "") {
      setAttributes([...attributes, newAttribute]);
      setNewAttribute("");
    }
  };

  const addVariant = () => {
    const newVariant = {};
    attributes.forEach(attr => {
      newVariant[attr] = '';
    });
    setVariants([...variants, newVariant]);
  };

  const removeVariant = (index) => setVariants(variants.filter((_, i) => i !== index));

  const removeAttribute = (index) => {
    const updatedAttributes = attributes.filter((_, i) => i !== index);
    setAttributes(updatedAttributes);

    const updatedVariants = updatedAttributes.length > 0 ?
      variants.map(variant => {
        const updatedVariant = { ...variant };
        delete updatedVariant[attributes[index]];
        return updatedVariant;
      }) : [];

    setVariants(updatedVariants);
  };

  const validateForm = () => {
    const errors = {};
    const seenVariants = new Set();

    if(!productName)
      errors["name"] = 'Este campo es obligatorio';
    if(productName.length > 80)
      errors["name"] = 'Este campo no puede ser mayor a 80 caracteres';

    variants.forEach((variant, variantIndex) => {
      attributes.forEach((attr) => {
        if (!variant[attr]) {
          if (!errors[variantIndex]) errors[variantIndex] = {};
          errors[variantIndex][attr] = 'Este campo es obligatorio';
        }
      });

      const variantValues = attributes.map((attr) => variant[attr]).join('-');
      if (seenVariants.has(variantValues)) {
        if (!errors[variantIndex]) errors[variantIndex] = {};
        errors[variantIndex].duplicate = 'Esta variante ya existe';
      } else {
        seenVariants.add(variantValues);
      }
    });

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  async function uploadImage(file) {
    const storageRef = ref(storage, `productPhotos/${file.name}`);

    // Subir archivo a Firebase Storage
    await uploadBytes(storageRef, file);

    // Obtener URL pública
    const url = await getDownloadURL(storageRef);
    return url;
  }


  const handlePhotoUpload = async (e) => {
    const file = e.target.files[0];
    if (file) {
      setUploading(true);
      setUploadError("");

      try {
        const url = await uploadImage(file); // Usar la nueva función
        setProductPhotoURL(url); // Guardar la URL pública

      } catch (error) {
        setUploadError("Error al cargar la foto. Por favor, inténtalo de nuevo.");
      } finally {
        setUploading(false);
      }
    }
  };


  const handleSubmit = (e) => {
      e.preventDefault();



      if (validateForm()) {
        const transformedVariants = variants.map(v =>
          Object.fromEntries(
            Object.entries(v).map(([key, value]) => [key.toLowerCase(), String(value).toLowerCase()])
          )
        );

        onSubmit( { productName: productName.toLowerCase(), variants: transformedVariants , productPhoto: productPhotoURL})
      }
  };

  return (
    <Form onSubmit={handleSubmit} className="p-4 border rounded">
      <InputField
        name="name"
        label="Nombre del producto"
        type="text"
        error={formErrors.name}
        onChange={handleProductNameChange}
        value={productName}
        className="mb-4"
      />

      <InputField
        name="productPhoto"
        label="Foto del producto"
        type="file"
        onChange={handlePhotoUpload}
        error={formErrors.productPhoto}
        className="mb-4"
      />
      
      <h4 className='fw-bold mt-3 mb-3'>Atributos</h4>

      <Row className="align-items-end mb-3">
        <Col md={5}>
          <InputField
            name="newAttribute"
            label="Nuevo Atributo"
            type="text"
            onChange={handleNewAttributeChange}
            value={newAttribute}
          />
        </Col>
        <Col md="auto">
          <Button variant="secondary" onClick={addAttribute}>
            <i className="bi bi-plus">  </i> Agregar Atributo
          </Button>
        </Col>
      </Row>

      {attributes.map((attr, index) => (
        <Row key={index} className="mb-2">
          <Col md={5}>
            <Form.Label><strong>{attr}</strong></Form.Label>
          </Col>
          <Col md="auto">
            <Button variant="danger" size="sm" onClick={() => removeAttribute(index)}>
              Eliminar
            </Button>
          </Col>
        </Row>
      ))}

      <h4 className='fw-bold mt-3 mb-3'>{"Variantes " + (attributes.length === 0 ? "- Crea nuevos atributos para empezar a agregar variantes" : "")}</h4>

      {attributes.length !== 0 &&
        <Button variant="outline-primary" onClick={addVariant} className="mb-3">
          <i className="bi bi-plus">  </i> Agregar Variante
        </Button>
      }
        {variants.map((variant, variantIndex) => (
          <Card className="mb-3" key={variantIndex}>
            <Card.Body>
              <Card.Title className="d-flex justify-content-between align-items-center">
                <span>Variante {variantIndex + 1}</span>
                <Button variant="danger" size="sm" onClick={() => removeVariant(variantIndex)}>
                  Eliminar variante
                </Button>
              </Card.Title>
              <Row>
                {attributes.map((attr, attrIndex) => (
                  <Col md={6} key={attrIndex} className="mb-3">
                    <InputField
                      name={attr}
                      label={attr}
                      type="text"
                      value={variant[attr]}
                      onChange={(e) => handleVariantChange(variantIndex, attr, e)}
                      error={formErrors[variantIndex] && formErrors[variantIndex][attr]}
                    />
                    <span className='text-danger p-3'>{formErrors[variantIndex] && formErrors[variantIndex].duplicate}</span>
                  </Col>
                ))}
              </Row>
            </Card.Body>
          </Card>
        ))}
      
      <Button variant="primary" type="submit" className="mt-3 w-100" size="lg">
        Crear producto
      </Button>
    </Form>
  );
}