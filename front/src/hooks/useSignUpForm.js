import { useState, useRef, useEffect } from 'react';
import { ref, uploadBytes, getDownloadURL } from "firebase/storage";
import { storage } from "../firebaseConfig";  

const VALID_GENDER_TYPES = ['Hombre', 'Mujer', 'Otro'];
const VALID_IMAGE_FORMATS = ['.png', '.jpg', '.jpeg'];

const validateForm = (data) => {
  const errors = {};
  if (!data.name) errors.name = 'El campo Nombre no puede estar vacío';
  if (data.name.length > 50) errors.name = 'El campo Nombre debe ser menor a 50 caracteres';

  if (!data.lastname) errors.lastname = 'El campo Apellido no puede estar vacío';
  if (data.lastname.length > 50) errors.lastname = 'El campo Apellido debe ser menor a 50 caracteres';

  if (!data.password) errors.password = 'El campo Contraseña no puede estar vacío';
  if (data.password.length > 50) errors.password = 'El campo Contraseña debe ser menor a 50 caracteres';

  if (!data.age) errors.age = 'El campo Edad no puede estar vacío';
  if (data.age < 0 || data.age > 120) errors.age = 'Ingrese una edad valida';
  if (isNaN(parseInt(data.age, 10))) errors.age = 'Ingrese una edad valida';

  if (!data.address) errors.address = 'El campo Direccion no puede estar vacío';
  if (!data.email) errors.email = 'El campo Correo electronico no puede estar vacío';

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(data.email)) {
    errors.email = 'Por favor, ingrese un email válido.';
  }
  if (data.email.length > 50) errors.email = 'El campo Email debe ser menor a 50 caracteres';

  if (!VALID_GENDER_TYPES.includes(data.gender)) errors.gender = 'Por favor, seleccione un género válido';

  if (!data.picture) errors.avatar = 'El campo foto de perfil no puede estar vacío';
  else {
    errors.avatar = 'Por favor ingrese una foto de perfil válida';
    VALID_IMAGE_FORMATS.forEach(e => {
      if (data.picture.name.toLowerCase().endsWith(e)) delete errors.avatar;
    });
  }

  return errors;
}

const uploadImage = async (pictureFile) => {
  const pictureRef = ref(storage, `images/${pictureFile.name}`);
  await uploadBytes(pictureRef, pictureFile);
  return getDownloadURL(pictureRef);
}

const useSignUpForm = (onSubmit) => {
  const [formErrors, setFormErrors] = useState({});
  const fieldRefs = {
    name: useRef(),
    lastname: useRef(),
    age: useRef(),
    gender: useRef(),
    address: useRef(),
    picture: useRef(),
    email: useRef(),
    password: useRef(),
  };

  useEffect(() => {
    fieldRefs.name.current.focus();
  }, [fieldRefs.name]);

  const handleOnSubmit = async (event) => {
    event.preventDefault();

    const formData = Object.keys(fieldRefs).reduce((data, key) => {
      data[key] = key === 'picture' ? fieldRefs[key].current.files[0] : fieldRefs[key].current.value;
      return data;
    }, {});

    const errors = validateForm(formData);
    setFormErrors(errors);

    if (Object.keys(errors).length === 0) {
      try {
        if (formData.picture) {
          formData.picture = await uploadImage(formData.picture);
        }
        onSubmit(formData);
      } catch (error) {
        setFormErrors({ avatar: 'Error uploading image: ' + error });
      }
    }
  };

  return { formErrors, fieldRefs, handleOnSubmit };
};

export default useSignUpForm;
