from keras.layers import Input, Dense
from keras.models import Model
import os
import matplotlib.pyplot as plt

# from keras.utils.visualize_util import plot
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

# this is the size of our encoded representations
encoding_dim = 32  # 32 floats -> compression of factor 24.5, assuming the input is 784 floats

# this is our input placeholder
input_img = Input(shape=(784,), name='in0')
# "encoded" is the encoded representation of the input
encoded = Dense(100, activation='relu')(input_img)
encoded = Dense(32, activation='relu')(encoded)
# "decoded" is the lossy reconstruction of the input
decoded1 = Dense(784, activation='sigmoid', name='decode1')(encoded)
decoded2 = Dense(784, activation='relu', name='decode2')(encoded)

ae = Model(input_img, outputs=[decoded1, decoded2])
losses = dict()
losses['decode1'] = 'binary_crossentropy'
losses['decode2'] = 'mse'
weights_ = [2.0, 0.3]

ae.compile(optimizer='sgd', loss=losses, loss_weights=weights_)
from keras.utils import plot_model

plot_model(ae, to_file='ae.png', show_shapes=True)
# this model maps an input to its reconstruction
# autoencoder = Model(input_img, decoded)

# this model maps an input to its encoded representation
# encoder = Model(input_img, encoded)

# create a placeholder for an encoded (32-dimensional) input
# encoded_input = Input(shape=(encoding_dim,))
# retrieve the last layer of the autoencoder model
# decoder_layer = autoencoder.layers[-1]
# create the decoder model
# decoder = Model(encoded_input, decoder_layer(encoded_input))
# sgd = optimizers.SGD(lr=0.01, decay=1e-6, momentum=0.9, nesterov=True)
# autoencoder.compile(optimizer='adadelta', loss='binary_crossentropy')
# sgd = optimizers.Adadelta(lr=1.0, rho=0.95, epsilon=1e-08, decay=0.0)
# autoencoder.compile(loss='binary_crossentropy', optimizer=sgd)


from keras.datasets import mnist
import numpy as np

(x_train, y_train), (x_test, y_test) = mnist.load_data()

x_train = x_train.astype('float32') / 255.
x_test = x_test.astype('float32') / 255.
x_train = x_train.reshape((len(x_train), np.prod(x_train.shape[1:])))
x_test = x_test.reshape((len(x_test), np.prod(x_test.shape[1:])))
in_data = dict()
in_data['in0'] = x_train
out_data = dict()
out_data['decode1'] = x_train
out_data['decode2'] = x_train

# total = np.sum(y_train.values())
# print total
# exit()

in_test = dict()
in_test['in0'] = x_test
out_test = dict()
out_test['decode1'] = x_test
out_test['decode2'] = x_test

# print x_train.shape, x_test.shape
# print y_train.shape, y_test.shape

# print x_train[0:10]
# print y_train[0:10]
# exit()

model = ae.fit(in_data, out_data, epochs=5, batch_size=256, shuffle=True
               , validation_data=(in_test, out_test))

losses_ = model.history['loss']
decode1_loss = model.history['decode1_loss']
print losses_
print type(losses_)
# validation_data=(x_test, x_test))

# encode and decode some digits
# note that we take them from the *test* set
# encoded_imgs = encoder.predict(x_test)
# decoded_imgs = decoder.predict(encoded_imgs)
#
# print encoded_imgs.shape
#
# # use Matplotlib (don't ask)
# import matplotlib.pyplot as plt
#
# n = 10  # how many digits we will display
# plt.figure(figsize=(20, 4))
# for i in range(n):
#     # display original
#     ax = plt.subplot(2, n, i + 1)
#     plt.imshow(x_test[i].reshape(28, 28))
#     plt.gray()
#     ax.get_xaxis().set_visible(False)
#     ax.get_yaxis().set_visible(False)
#
#     # display reconstruction
#     ax = plt.subplot(2, n, i + 1 + n)
#     plt.imshow(decoded_imgs[i].reshape(28, 28))
#     plt.gray()
#     ax.get_xaxis().set_visible(False)
#     ax.get_yaxis().set_visible(False)
# plt.show()
