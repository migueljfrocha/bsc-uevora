###################################################################################################################################################
# 
# Authors: Miguel Pombeiro (57829), Miguel Rocha (58501)
# Course: Arquitetura de Computadores I
# School Year: 2023/2024
# 
# Este programa em assembly RISC-V reduz o ruido de uma imagem utilizando um filtro de media ou de mediana.
# Os filtros foram implementados em assembly RISC-V de acordo com as instrucoes fornecidas no enunciado do trabalho.
#
###################################################################################################################################################
.globl main

###################################################################################################################################################
#
# Macro: Ending
# Descricao: Esta macro termina o programa e imprime uma mensagem de erro, em determinadas situacoes.
# Argumentos:
# %d - Codigo de terminacao
# %s - Mensagem de terminacao
#
###################################################################################################################################################
.macro Ending (%d, %s) 
	.data
Ending_String: .string %s
			   .align 2

	.text
	li a7, 4
	la a0, Ending_String
	ecall
	li a7, 93
	li a0, %d
	ecall
.end_macro

.data

# NOMES DOS FICHEIROS - devem ser alterados de acordo com os nomes desejados para os ficheiros de input e output.
# Nome do ficheiro .gray corrompido com ruido, para filtrar.
input_name_gray:  .string "cat_noisy.gray"
			      .align 2
# Nome para o ficheiro .gray do output, com imagem filtrada.
output_name_gray: .string "result.gray"
  		 	      .align 2


# TEXTOS PARA O MENU
prompt_text: 	.string "Qual filtro deseja utilizar?\n0-Sair\n1-Media \n2-Mediana "
			    .align 2
confirm_text:   .string "Deseja sair ?"
			    .align 2

# Espaco reservado em memoria para as imagens original e filtrada, respetivamente.
original_image: .space 239600 #599x400
		  	 .align 2
final_image: .space 239600
		 	 .align 2

# Espaco reservado em memoria para o array auxiliar usado em calculos de mediana.
median_space: .space 9
		      .align 2

.text

###################################################################################################################################################
#
# Funcao: FUNC_read_gray_image
# Descricao: Esta funcao recebe uma string com o caminho para a imagem em formato .gray, abre o ficheiro e le a imagem para memoria.
# Argumentos:
# a0 - Endereco de memoria que contem a string com o caminho para a imagem
# Retorna:
# a0 - Endereco de memoria para onde foi lido o ficheiro .gray
#
###################################################################################################################################################
FUNC_read_gray_image:
addi sp, sp, -16
sw s3, 12(sp)
sw s2, 8(sp)
sw s1, 4(sp)
sw ra, 0(sp)


li s3, -1

# Abrir a imagem em .gray no modo de leitura
	li a7, 1024
	li a1, 0
	ecall

beq a0, s3, Not_Read

	mv s1, a0

# Ler e guardar os pixeis da imagem em memoria
	li a7, 63
	la a1, original_image
	mv s2, a1
	li a2, 239600             # tamanho da imagem
	ecall       	

# Fechar o ficheiro
	li a7, 57
	mv a0, s1
	ecall
  
	mv a0, s2
j Read_Ok

Not_Read:
	Ending (-1, "Nao foi possivel ler o ficheiro.\n")


Read_Ok:
lw s3, 12(sp)
lw s2, 8(sp)
lw s1, 4(sp)
lw ra, 0(sp)
addi sp, sp, 16
ret

###################################################################################################################################################
#
# Funcao: FUNC_median_filter
# Descricao: Esta funcao aplica um filtro de mediana a uma imagem, de forma a reduzir o ruido.
# Argumentos:
# a0 - Endereco de memoria onde esta guardada a imagem orginal
# a1 - Largura da imagem
# a2 - Endereco onde sera guardada a imagem filtrada
# a3 - Tamanho da imagem original
# Retorna:
# Nao retorna nada
#
###################################################################################################################################################
FUNC_median_filter:
addi sp, sp, -40
sw s0, 0(sp)
sw s1, 4(sp)
sw s2, 8(sp)
sw s3, 12(sp)
sw s4, 16(sp)
sw s5, 20(sp)
sw s6, 24(sp)
sw s7, 28(sp)
sw s8, 32(sp)
sw ra, 36(sp)


# Copia dos argumentos para registos auxiliares, s
mv s0, a0 
mv s1, a1 
mv s8, a2 
mv s2, a3 
la s7, median_space 

sub s6, s2, s1 

mv s4, zero
Median_Row_Iteration:

	# Condicoes para nao aplicar o filtro nas margens da imagem
	beqz s4, Median_Row_Continue 
	beq s4, s6, Median_Row_Continue

	mv s3, zero
Median_Column_Iteration:

	# Condicoes para nao aplicar o filtro nas margens da imagem
	addi t2, s1, -1
	beqz s3, Median_Column_Continue 
	beq s3, t2, Median_Column_Continue

		mv t3, s7 
		sub t4, s4, s1 
		add t5, s4, s1 
Median_KernelRow_Iteration:
 
			addi s5, s3, -1 
			addi t6, s3, 1 
Median_KernelColumn_Iteration:
 
				# Insercao dos valores das intensidades no array mediana
				add t0, s0, t4
				add t0, t0, s5
				lbu t1, 0(t0)
				sb t1, 0(t3)
				addi t3, t3, 1

				addi s5, s5, 1
				ble s5, t6, Median_KernelColumn_Iteration

			add t4, t4, s1
			ble t4, t5, Median_KernelRow_Iteration

		# Calculo da mediana que e guardada no endereco da imagem final
		mv a0, s7 
		li a1, 9 
		jal FUNC_median_of_nine # retorna a0 - mediana
		add t1, s8, s3
		add t1, t1, s4
		sb a0, 0(t1)

Median_Column_Continue:
		addi s3, s3, 1
		blt s3, s1, Median_Column_Iteration
	
Median_Row_Continue:
	add s4, s4, s1
	blt s4, s2, Median_Row_Iteration


lw s0, 0(sp)
lw s1, 4(sp)
lw s2, 8(sp)
lw s3, 12(sp)
lw s4, 16(sp)
lw s5, 20(sp)
lw s6, 24(sp)
lw s7, 28(sp)
lw s8, 32(sp)
lw ra, 36(sp)
addi sp, sp, 40
ret

###################################################################################################################################################
#
# Funcao: FUNC_mean_filter
# Descricao: Esta funcao aplica um filtro de media a uma imagem, de forma a reduzir o ruido.
# Argumentos:
# a0 - Endereco de memoria onde esta guardada a imagem orginal
# a1 - Largura da imagem
# a2 - Endereco onde sera guardada a imagem filtrada
# a3 - Tamanho da imagem original
# Retorna:
# Nao retorna nada.
#
###################################################################################################################################################
FUNC_mean_filter:
addi sp, sp, -40
sw s0, 0(sp)
sw s1, 4(sp)
sw s2, 8(sp) 
sw s3, 12(sp)
sw s4, 16(sp)
sw s5, 20(sp)
sw s6, 24(sp)
sw s7, 28(sp)
sw s8, 32(sp)
sw ra, 36(sp)


# Copia dos argumentos para registos auxiliares, s
mv s0, a0
mv s1, a1
mv s8, a2
mv s2, a3

sub s6, s2, s1

mv s4, zero
Mean_Row_Iteration:

	# Condicoes para nao aplicar o filtro nas margens da imagem
	beqz s4, Mean_Row_Continue
	beq s4, s6, Mean_Row_Continue

	mv s3, zero
Mean_Column_Iteration:

	# Condicoes para nao aplicar o filtro nas margens da imagem
	addi t2, s1, -1
	beqz s3, Mean_Column_Continue
	beq s3, t2, Mean_Column_Continue

		mv s7, zero
		sub t4, s4, s1
		add t5, s4, s1
Mean_KernelRow_Iteration: 

			addi s5, s3, -1 
			addi t6, s3, 1 
Mean_KernelColumn_Iteration: 
				# Soma dos valores das intensidades dos pixeis
				add t0, s0, t4
				add t0, t0, s5
				lbu t1, 0(t0)
				add s7, s7, t1
			
				addi s5, s5, 1
				ble s5, t6, Mean_KernelColumn_Iteration

			add t4, t4, s1
			ble t4, t5, Mean_KernelRow_Iteration

		# Calculo da media que e guardada no endereco da imagem final
		mv a0, s7
		jal FUNC_mean_of_nine #retorna a0 - media
		mv s7, a0 
		add t1, s8, s3 
		add t1, t1, s4
		sb s7, 0(t1) 
	
Mean_Column_Continue:
		addi s3, s3, 1
		blt s3, s1, Mean_Column_Iteration
	
Mean_Row_Continue:
	add s4, s4, s1
	blt s4, s2, Mean_Row_Iteration


lw s0, 0(sp)
lw s1, 4(sp)
lw s2, 8(sp)
lw s3, 12(sp)
lw s4, 16(sp)
lw s5, 20(sp)
lw s6, 24(sp)
lw s7, 28(sp)
lw s8, 32(sp)
lw ra, 36(sp)
addi sp, sp, 40
ret

###################################################################################################################################################
#
# Funcao: FUNC_write_gray_image
# Descricao: Esta funcao escreve a imagem filtrada num ficheiro em formato .gray.
# Argumentos:
# a0 - Endereco da string com o nome do ficheiro a guardar
# a1 - Endereco da imagem a guardar no ficheiro (com o filtro aplicado)
# Retorna:
# Nao retorna nada.
#
###################################################################################################################################################
FUNC_write_gray_image:
addi sp, sp, -16
sw s3, 12(sp)
sw s2, 8(sp)
sw s1, 4(sp)
sw ra, 0(sp)


li s3, -1
mv s2, a1

#Abre o ficheiro novo (se nao existe, cria esse ficheiro)
	li a7, 1024
	li a1, 1
	ecall
	mv s1, a0

beq a0, s3, Not_written
  
# Escreve os pixeis da imagem 
	li a7, 64
	mv a1, s2
	li a2, 239600             # tamanho da imagem final (igual ao tamanho da imagem original)
	ecall
  
# Fecha o ficheiro
	li a7, 57
	mv a0, s1
	ecall
j Written_Ok

Not_written:
	Ending (-2, "Nao foi possivel guardar o novo ficheiro.\n")


Written_Ok:
lw s3, 12(sp)
lw s2, 8(sp)
lw s1, 4(sp)
lw ra, 0(sp)
addi sp, sp, 16
ret
  
###################################################################################################################################################
#
# Funcao: FUNC_median_of_nine
# Descricao: Esta funcao utiliza uma versao de Selection Sort de forma a ordenar um array de nove elementos e a devolver o elemento no indice 5.
#            Calcula, efetivamente, a mediana dos 9 elementos.
# Argumentos:
# a0 - Endereco do array para ordenar
# a1 - Tamanho do array para ordenar 
# Retorna:
# a0 - Mediana de 9 elementos
#
###################################################################################################################################################
FUNC_median_of_nine:
addi sp, sp, -8
sw s0, 0(sp)
sw s1, 4(sp)


	li t0, 0
	li t3, 5 # indice da mediana
Sort_Selector:

		# Prepara os indices para a ordenacao
		mv t1, t0 
		mv s0, a0 
		mv s1, a0
Sort_Iterator:
		lbu t4, 0(s0) 
		lbu t5, 0(s1) 

		bgt t4, t5, Sort_Find_Minimum 
		mv s1, s0 
Sort_Find_Minimum:

		addi t1, t1, 1 
		addi s0, s0, 1 
		blt t1, a1, Sort_Iterator

	# Troca dos valores
	lbu t4, 0(a0) 
	lbu t5, 0(s1) 
	sb t5, 0(a0)
	sb t4, 0(s1) 

	addi a0, a0, 1 
	addi t0, t0, 1 
	blt t0, t3, Sort_Selector

# Retorna o valor da mediana
addi a0, a0, -1
lbu a0, 0(a0)


lw s0, 0(sp)
lw s1, 4(sp)
addi sp, sp, 8
ret

###################################################################################################################################################
#
# Funcao: FUNC_mean_of_nine
# Descricao: Esta funcao calcula realiza subtracoes sucessivas por 9 de forma a calcular a sua divisao inteira por 9. 
#            Calcula, efetivamente, a media de 9 elementos.
# Argumentos:
# a0 - Somatorio de 9 elementos
# Retorna:
# a0 - Media 
#
###################################################################################################################################################
FUNC_mean_of_nine:

mv t0, a0 
mv a0, zero
li t1, 9 # numero pelo qual se prentende dividir

blt t0, t1, Mean_Out

Divide_By_Nine:
	sub t0, t0, t1
	addi a0, a0, 1
	bge t0, t1, Divide_By_Nine

Mean_Out:
ret

###################################################################################################################################################
#
# Funcao: FUNC_menu
# Descricao: Esta funcao realiza um prompt ao utilizador para perguntar qual o tipo de filtro que quer utilizar na
#            imagem. Se nao for inserida uma selecao valida (0, 1, 2) pergunta ao utilizador se deseja terminar a execucao.        
# Argumentos:
# Nao tem.
# Retorna:
# a0 - Escolha do utilizador (1-media; 2-mediana) 
#
###################################################################################################################################################
FUNC_menu:
addi sp, sp, -8
sw ra, 0(sp)
sw s0, 4(sp)


li s0, 3
PROMPT:
la a0, prompt_text
li a7, 51
ecall

# Verifica se a escolha do utilizador e valida
bltz a0, PROMPT 
bge a0, s0, PROMPT 

bnez a0, Escolha_valida

# Confirmacao para saida do programa 
li a7, 50
la a0, confirm_text
ecall

# Se o utilizador desejar sair, termina o programa
bnez a0, PROMPT
	Ending (-3, "Programa terminado pelo utilizador.\n")

Escolha_valida:

lw ra, 0(sp)
lw s0, 4(sp)
addi sp, sp, 8
ret

###################################################################################################################################################
#
# Funcao: main
# Descricao: Esta funcao contem a dinamica geral do programa. 
#            Aqui sao chamadas todas as outras funcoes que permitem a aplicacao dos filtros a imagem original.
# Argumentos:
# Nao tem.
# Retorna:
# Nao retorna nada. 
#
###################################################################################################################################################
main:
addi sp, sp -8
sw ra, 4(sp)
sw s1, 0(sp)

# Menu para escolha do filtro a aplicar
jal FUNC_menu
mv s1, a0

# Leitura da imagem original para memoria
la a0, input_name_gray
jal FUNC_read_gray_image

li a1, 599
la a2, final_image
li a3, 239600

li t0, 2

# Aplicacao do filtro de media ou de mediana, consoante a escolha do utilizador
beq s1, t0, Median_Run
jal FUNC_mean_filter
j Mean_Jump

Median_Run:
jal FUNC_median_filter

# Escrita da imagem filtrada num ficheiro .gray
Mean_Jump:
la a0, output_name_gray
la a1, final_image
jal FUNC_write_gray_image


lw ra, 4(sp)
lw s1, 0(sp)
addi sp, sp 8

Ending (0, "Filtro aplicado com sucesso!")
