import matplotlib.pyplot as plt
from sklearn.datasets import make_circles
import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.optimizers import SGD
import numpy as np
import time


def create_model_slp(input_shape):
    model = Sequential([
        Dense(1, input_shape=input_shape, activation='sigmoid')
    ])
    model.compile(optimizer=SGD(learning_rate=0.1),
                  loss='mse', metrics=['accuracy'])
    return model


def create_model_mlp(input_shape):
    model = Sequential([
        Dense(10, input_shape=input_shape, activation='relu'),
        Dense(1, activation='sigmoid')
    ])
    model.compile(optimizer=SGD(learning_rate=0.1),
                  loss='mse', metrics=['accuracy'])
    return model


def create_model_mlp_node(input_shape, nodes_per_layer):
    model = Sequential()
    for _ in range(2):
        model.add(Dense(nodes_per_layer, activation='relu',
                  input_shape=input_shape))
    model.add(Dense(1, activation='sigmoid'))
    model.compile(optimizer=SGD(learning_rate=0.1),
                  loss='mse', metrics=['accuracy'])
    return model


def train_and_evaluate(model, x_train, y_train, x_test, y_test):
    start_time = time.time()
    model.fit(x_train, y_train, epochs=300, batch_size=1, verbose=0)
    training_time = time.time() - start_time
    test_loss, test_acc = model.evaluate(x_test, y_test, verbose=0)
    return test_acc, test_loss, training_time


def plot_results(model, x_test, y_test, title):
    predictions = model.predict(x_test) > 0.5  # 0.5를 기준으로 True는 1, False는 0
    plt.figure(figsize=(6, 6))
    plt.scatter(x_test[predictions.flatten(), 0], x_test[predictions.flatten(), 1],
                c='purple', label='Predicted Class 1', alpha=0.7)  # 예측된 클래스 1
    plt.scatter(x_test[~predictions.flatten(), 0], x_test[~predictions.flatten(), 1],
                c='yellow', label='Predicted Class 0', alpha=0.7)  # 예측된 클래스 0
    plt.title(title)
    plt.xlabel('Feature 1')
    plt.ylabel('Feature 2')
    plt.legend()
    plt.show()


def CircleClassify():
    n_samples = 400
    noise = 0.02
    factor = 0.5
    x_train, y_train = make_circles(
        n_samples=n_samples, noise=noise, factor=factor)
    x_test, y_test = make_circles(
        n_samples=n_samples, noise=noise, factor=factor)

    plt.scatter(x_train[:, 0], x_train[:, 1], c=y_train, marker='.')
    plt.title("Training Data Distribution")
    plt.show()

    # Write your codes here - begin
    # Create and train SLP
    slp = create_model_slp((2,))
    _, loss, training_time = train_and_evaluate(
        slp, x_train, y_train, x_test, y_test)
    print(
        f"SLP : Loss = {loss}, Training Time: {training_time:.2f} seconds")
    plot_results(slp, x_test, y_test, "SLP Prediction Results")

    # Create and train MLP
    mlp = create_model_mlp((2,))
    _, loss, training_time = train_and_evaluate(
        mlp, x_train, y_train, x_test, y_test)
    print(
        f"MLP : Loss = {loss}, Training Time: {training_time:.2f} seconds")
    plot_results(mlp, x_test, y_test, "MLP Prediction Results")
    # Write your codes here - end


def number4():
    n_samples = 400
    noise = 0.02
    factor = 0.5
    x_train, y_train = make_circles(
        n_samples=n_samples, noise=noise, factor=factor)
    x_test, y_test = make_circles(
        n_samples=n_samples, noise=noise, factor=factor)

    configurations = [3, 5, 10]

    for nodes in configurations:
        mlp = create_model_mlp_node((2,), nodes)
        _, loss, training_time = train_and_evaluate(
            mlp, x_train, y_train, x_test, y_test)
        print(
            f"Nodes: {nodes}, Loss: {loss}, Training Time: {training_time:.2f} seconds")


if __name__ == '__main__':
    CircleClassify()
    number4()
