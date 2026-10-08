# Brain Tumor Diagnosis - Machine Learning

A machine learning project developed for the **Apparent Diffusion Coefficient (ADC)** MRI analysis challenge and project for the Machine Learning course at Universidade de Évora.

## About

This project implements machine learning models to assist in the diagnosis of brain tumors based on demographic data and texture characteristics extracted from **ADC (Apparent Diffusion Coefficient)** MRI slices.

The main objective is to predict the diagnosis (Benign vs. Malignant) by analyzing patient data and maximizing the **F1 Score**. The complexity lies in the dataset structure, which contains multiple slices per patient but requires a patient-level diagnosis.


## Technologies

- **Language:** Python
- **Libraries:** scikit-learn, pandas, numpy, seaborn, matplotlib

---

## How to Run

### Prerequisites
- Python 3.8+
- [Jupyter Notebook](https://jupyter.org/)
- Dependencies: pandas, numpy, scikit-learn, matplotlib, seaborn
  
### Steps
**1. Open the main analysis notebook:**
```bash
jupyter notebook report.ipynb
```
**2. Run the cells in order to reproduce the preprocessing pipelines and model training.**

**3. Check the results**


---


## Grade

[![Grade](https://img.shields.io/badge/Grade-5.8%2F6.0-brightgreen)]()


*Machine Learning - 2025/2026*  
*Universidade de Évora*

## Authors

- [**Miguel Pombeiro**](https://github.com/MiguelPombeiro)
- [**Miguel Rocha**](https://github.com/migueljfrocha)
- [**Rodrigo Santos**](https://github.com/rodrigosantos46320)

---

## Additional Notes

Some models can take a while to run due to their complexity.

### Possible Improvements
- Implement a custom GridSearch that optimizes hyperparameters based on patient-level metrics rather than slice-level accuracy.
- Conduct a deeper analysis to apply changes to the model according to medical knowledge
- Test more models and fine-tune the parameters
