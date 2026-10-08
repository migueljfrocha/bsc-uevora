const insertOfferLink = "https://magno.di.uevora.pt/tweb/t1/oferta/insert";

const contentDiv = document.getElementById("content");
let xhr = new XMLHttpRequest();


/**
 * Renders the form to add a new offer.
 */
function showAddOfferForm() {
  contentDiv.innerHTML = `
    <div id="add-offer-container">
      <h1>Adicionar Ofertas</h1>
      <hr/>
      <form id="add-offer-form" onsubmit="return false;">
        <div class="form-group">
          <label for="name">Nome da oferta:</label>
          <input type="text" id="name" name="nome" onfocus="removeError(this)" required>
        </div>

        <div class="form-group">
          <label for="generic-id">ID do restaurante:</label>
          <input type="number" id="generic-id" name="restaurante_id" onfocus="removeError(this)" required>
        </div>

        <div class="form-group">
          <label for="photo">Fotografia:</label>
          <input type="text" id="photo" name="foto" onfocus="removeError(this)" required>
        </div>

        <div class="form-group">
          <label for="descricao">Descrição:</label>
          <textarea name="descricao" id="description" rows="10" cols="50" onfocus="removeError(this)" required></textarea>
        </div>

        <div class="form-group">
          <label for="unidades">Unidades:</label>
          <input type="number" id="units" name="unidades" onfocus="removeError(this)" required>
        </div>

        <button type="button" id="add-offer-button" onclick="submitForm()">Adicionar Oferta</button>
      </form>
    </div>
  `;
}


/**
 * Validates the add offer form.
 * If there are validation errors, displays error messages next to the relevant fields.
 * @returns {boolean} True if the form is valid, false otherwise.
 */
function validateForm() {
  // Remove any existing error messages
  const existingErrors = document.querySelectorAll(".error-message");
  existingErrors.forEach((error) => error.remove());

  // Remove error styling
  const errorInputs = document.querySelectorAll(".input-error");
  errorInputs.forEach((input) => input.classList.remove("input-error"));

  let isValid = true;

  // Get form fields
  const name = document.getElementById("name");
  const restaurantId = document.getElementById("generic-id");
  const photo = document.getElementById("photo");
  const description = document.getElementById("description");
  const units = document.getElementById("units");

  // Validate name
  if (name.value.trim() === "") {
    showError(name, "O nome da oferta é obrigatório");
    isValid = false;
  }

  // Validate restaurant ID
  if (restaurantId.value.trim() === "") {
    showError(restaurantId, "O ID do restaurante é obrigatório");
    isValid = false;
  } else if (restaurantId.value <= 0) {
    showError(restaurantId, "O ID do restaurante deve ser maior que 0");
    isValid = false;
  }

  // Validate photo URL
  if (photo.value.trim() === "") {
    showError(photo, "A URL da fotografia é obrigatória");
    isValid = false;
  }

  // Validate description
  if (description.value.trim() === "") {
    showError(description, "A descrição é obrigatória");
    isValid = false;
  } else if (description.value.trim().length < 10) {
    showError(description, "A descrição deve ter pelo menos 10 caracteres");
    isValid = false;
  }

  // Validate units
  if (units.value.trim() === "") {
    showError(units, "O número de unidades é obrigatório");
    isValid = false;
  } else if (units.value <= 0) {
    showError(units, "O número de unidades deve ser maior que 0");
    isValid = false;
  }

  return isValid;
}


/**
 * Renders an error message next to the given input element.
 * @param {HTMLElement} inputElement 
 * @param {string} message 
 */
function showError(inputElement, message) {
  // Add error styling to input
  inputElement.classList.add("input-error");

  // Create error message element
  const errorDiv = document.createElement("div");
  errorDiv.className = "error-message";
  errorDiv.textContent = message;

  // Insert error message after the input
  inputElement.parentElement.appendChild(errorDiv);
}


/**
 * Removes the error styling from the given input element.
 * @param {HTMLElement} element 
 */
function removeError(element) {
  element.classList.remove("input-error");
}


/**
 * Submits the add offer form after validation.
 * On success, shows a success message.
 * On failure, shows an error message.
 */
function submitForm() {
  // Validate form
  if (!validateForm()) {
    return; // Stop if validation fails
  }

  // If validation passes, submit the form
  const formData = new FormData(document.getElementById("add-offer-form"));
  const data = new URLSearchParams(formData).toString();

  xhr.open("POST", insertOfferLink, true);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

  xhr.onreadystatechange = function () {
    if (this.readyState === 4 && this.status === 200) {
      const response = JSON.parse(this.responseText);

      if (response.status === "ok") {
        showSuccessMessage(response.oferta_id);
      } else {
        showErrorMessage(response.status || "Erro desconhecido");
      }
    }
  };

  xhr.send(data);
}


/**
 * Displays a success message after adding an offer.
 * @param {number} ofertaId 
 */
function showSuccessMessage(ofertaId) {
  contentDiv.innerHTML = `
    <div id="add-offer-container">
      <div class="success-card">
        <div class="success-header">
          <h3>Oferta adicionada com Sucesso!</h3>
        </div>
        <span class="success-id">ID ${ofertaId}</span>
        <button 
          id="add-offer-button" 
          onclick="showAddOfferForm()"
        >
          Adicionar Nova Oferta
        </button>
      </div>
    </div>
  `;
}


/**
 * Displays an error message below the submit button.
 * @param {string} message 
 */
function showErrorMessage(message) {
  // Remove any existing error message
  const existingError = document.querySelector(".submit-error");
  if (existingError) {
    existingError.remove();
  }

  // Create error message
  const errorDiv = document.createElement("div");
  errorDiv.className = "submit-error";
  errorDiv.textContent = message;

  // Insert after the submit button
  const button = document.getElementById("add-offer-button");
  button.parentElement.appendChild(errorDiv);
}
